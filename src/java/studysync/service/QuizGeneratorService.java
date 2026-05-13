package studysync.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import studysync.model.Question;

public class QuizGeneratorService {

    private static final String MODEL = "gemini-1.5-flash";

    public List<Question> generateQuestions(int nodeId, String studyContent)
            throws IOException {

        String apiKey = getApiKey();
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/"
                      + MODEL + ":generateContent?key=" + apiKey;

        String prompt = "You are a university quiz generator. "
                + "Generate exactly 5 multiple choice questions based on this study material:\\n\\n"
                + escapeJson(studyContent)
                + "\\n\\nRules:\\n"
                + "- Each question must have exactly 4 options (A, B, C, D)\\n"
                + "- Only one option is correct\\n"
                + "- Questions must test understanding, not just memorisation\\n"
                + "- Respond ONLY with a valid JSON array, no extra text, no markdown backticks\\n"
                + "- Use this exact format:\\n"
                + "[{\\\"question\\\":\\\"Question text?\\\","
                + "\\\"a\\\":\\\"Option A\\\","
                + "\\\"b\\\":\\\"Option B\\\","
                + "\\\"c\\\":\\\"Option C\\\","
                + "\\\"d\\\":\\\"Option D\\\","
                + "\\\"answer\\\":\\\"A\\\"}]";

        String requestBody = "{"
                + "\"contents\":[{"
                + "\"parts\":[{\"text\":\"" + prompt + "\"}]"
                + "}]}";

        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(60000);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        InputStream is = status == 200 ? conn.getInputStream() : conn.getErrorStream();
        String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        if (status != 200) {
            throw new IOException("Gemini API error " + status + ": " + response);
        }

        return parseQuestions(response, nodeId);
    }

    private List<Question> parseQuestions(String apiResponse, int nodeId) {
        List<Question> questions = new ArrayList<>();
        try {
            int textStart = apiResponse.indexOf("\"text\": \"");
            if (textStart == -1) textStart = apiResponse.indexOf("\"text\":\"");
            if (textStart == -1) return questions;

            textStart = apiResponse.indexOf("\"", textStart + 7) + 1;
            int textEnd = apiResponse.indexOf("\"\n", textStart);
            if (textEnd == -1) textEnd = apiResponse.lastIndexOf("\"");

            String jsonText = apiResponse.substring(textStart, textEnd);

            jsonText = jsonText.replace("\\n", "")
                               .replace("\\\"", "\"")
                               .replace("\\\\", "\\")
                               .trim();

            int arrayStart = jsonText.indexOf("[");
            int arrayEnd   = jsonText.lastIndexOf("]");
            if (arrayStart == -1 || arrayEnd == -1) return questions;
            jsonText = jsonText.substring(arrayStart, arrayEnd + 1);

            int pos = 0;
            while (pos < jsonText.length()) {
                int objStart = jsonText.indexOf("{", pos);
                if (objStart == -1) break;

                int depth = 0;
                int objEnd = objStart;
                for (int i = objStart; i < jsonText.length(); i++) {
                    if (jsonText.charAt(i) == '{') depth++;
                    else if (jsonText.charAt(i) == '}') {
                        depth--;
                        if (depth == 0) { objEnd = i; break; }
                    }
                }

                String obj = jsonText.substring(objStart, objEnd + 1);

                Question q = new Question();
                q.setNodeId(nodeId);
                q.setQuestionText(extractValue(obj, "question"));
                q.setOptionA(extractValue(obj, "a"));
                q.setOptionB(extractValue(obj, "b"));
                q.setOptionC(extractValue(obj, "c"));
                q.setOptionD(extractValue(obj, "d"));
                q.setCorrectAnswer(extractValue(obj, "answer").toUpperCase());

                if (!q.getQuestionText().isEmpty()
                        && !q.getOptionA().isEmpty()
                        && !q.getCorrectAnswer().isEmpty()) {
                    questions.add(q);
                }

                pos = objEnd + 1;
            }

        } catch (Exception e) {
            // parse error — return empty list
        }

        return questions;
    }

    private String extractValue(String json, String key) {
        String search = "\"" + key + "\": \"";
        int start = json.indexOf(search);
        if (start == -1) {
            search = "\"" + key + "\":\"";
            start = json.indexOf(search);
        }
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return "";
        return json.substring(start, end);
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private String getApiKey() throws IOException {
        try (InputStream is = QuizGeneratorService.class
                .getClassLoader()
                .getResourceAsStream("api.properties")) {
            if (is == null) throw new IOException("api.properties not found");
            Properties props = new Properties();
            props.load(is);
            return props.getProperty("gemini.api.key");
        }
    }
}