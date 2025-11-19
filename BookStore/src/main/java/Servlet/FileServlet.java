package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Serve files saved in user's home BookStoreUploads folder at:
 *   http://localhost:8080/BookStore/BookStoreUploads/<filename>
 */
@WebServlet("/BookStore/BookStoreUploads/*")
public class FileServlet extends HttpServlet {

    // This must match the folder your bean writes to:
    private final String UPLOAD_FOLDER = System.getProperty("user.home") + File.separator + "BookStoreUploads";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request.getPathInfo() returns "/filename.ext"
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // remove leading slash and decode URL (handle %20 etc.)
        String filename = pathInfo.substring(1);
        filename = URLDecoder.decode(filename, "UTF-8");

        File file = new File(UPLOAD_FOLDER, filename);
        if (!file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Try to guess content type
        String mime = Files.probeContentType(Path.of(file.getAbsolutePath()));
        if (mime == null) {
            // fallback for common images
            if (filename.toLowerCase().endsWith(".png")) mime = "image/png";
            else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) mime = "image/jpeg";
            else if (filename.toLowerCase().endsWith(".gif")) mime = "image/gif";
            else mime = "application/octet-stream";
        }
        resp.setContentType(mime);
        resp.setContentLengthLong(file.length());

        // Stream the file
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream out = new BufferedOutputStream(resp.getOutputStream())) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
    }
}
