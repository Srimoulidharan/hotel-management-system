package com.hotel.servlet;

import com.hotel.dao.GuestDAO;
import com.hotel.model.Guest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/guests")
public class GuestServlet extends HttpServlet {
    private final GuestDAO guestDAO = new GuestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("guests", guestDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/views/guests.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                guestDAO.create(readGuest(request, false));
                redirect(response, request, "Guest added successfully.", null);
            } else if ("update".equals(action)) {
                guestDAO.update(readGuest(request, true));
                redirect(response, request, "Guest updated successfully.", null);
            } else if ("delete".equals(action)) {
                guestDAO.delete(Integer.parseInt(request.getParameter("id")));
                redirect(response, request, "Guest deleted successfully.", null);
            } else {
                redirect(response, request, null, "Invalid guest action.");
            }
        } catch (Exception e) {
            redirect(response, request, null, e.getMessage());
        }
    }

    private Guest readGuest(HttpServletRequest request, boolean includeId) {
        Guest guest = new Guest();
        if (includeId) {
            guest.setId(Integer.parseInt(request.getParameter("id")));
        }
        guest.setFullName(request.getParameter("fullName"));
        guest.setEmail(request.getParameter("email"));
        guest.setPhone(request.getParameter("phone"));
        guest.setIdProof(request.getParameter("idProof"));
        guest.setAddress(request.getParameter("address"));
        return guest;
    }

    private void redirect(HttpServletResponse response, HttpServletRequest request, String success, String error) throws IOException {
        String param = success != null ? "success=" + encode(success) : "error=" + encode(error);
        response.sendRedirect(request.getContextPath() + "/guests?" + param);
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
