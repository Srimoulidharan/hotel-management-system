package com.hotel.servlet;

import com.hotel.dao.RoomDAO;
import com.hotel.model.Room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {
    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("rooms", roomDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/views/rooms.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                roomDAO.create(readRoom(request, false));
                redirect(response, request, "Room added successfully.", null);
            } else if ("update".equals(action)) {
                roomDAO.update(readRoom(request, true));
                redirect(response, request, "Room updated successfully.", null);
            } else if ("delete".equals(action)) {
                roomDAO.delete(Integer.parseInt(request.getParameter("id")));
                redirect(response, request, "Room deleted successfully.", null);
            } else {
                redirect(response, request, null, "Invalid room action.");
            }
        } catch (Exception e) {
            redirect(response, request, null, e.getMessage());
        }
    }

    private Room readRoom(HttpServletRequest request, boolean includeId) {
        Room room = new Room();
        if (includeId) {
            room.setId(Integer.parseInt(request.getParameter("id")));
        }
        room.setRoomNumber(request.getParameter("roomNumber"));
        room.setRoomType(request.getParameter("roomType"));
        room.setPricePerNight(new BigDecimal(request.getParameter("pricePerNight")));
        room.setStatus(request.getParameter("status"));
        room.setDescription(request.getParameter("description"));
        return room;
    }

    private void redirect(HttpServletResponse response, HttpServletRequest request, String success, String error) throws IOException {
        String param = success != null ? "success=" + encode(success) : "error=" + encode(error);
        response.sendRedirect(request.getContextPath() + "/rooms?" + param);
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
