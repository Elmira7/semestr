package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/images/*")
public class PhotoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = "C:\\Users\\Elmira\\Desktop\\img\\";
        String imageName = request.getPathInfo().substring(1);

        File file = new File(path + imageName);
        byte[] content = Files.readAllBytes(file.toPath());

        response.setContentType(getServletContext().getMimeType(imageName));
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
    }

}
