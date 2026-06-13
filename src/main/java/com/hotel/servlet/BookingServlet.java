package com.hotel.servlet;

import com.hotel.dao.BookingDAO;
import com.hotel.dao.GuestDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Booking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@WebServlet("/bookings")
public class BookingServlet extends HttpServlet {
    private final BookingDAO bookingDAO = new BookingDAO();
    private final GuestDAO guestDAO = new GuestDAO();
    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("bookings", bookingDAO.findAllWithDetails());
            request.setAttribute("guests", guestDAO.findAll());
            request.setAttribute("rooms", roomDAO.findBookableRooms());
            request.getRequestDispatcher("/WEB-INF/views/bookings.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                Booking booking = new Booking();
                booking.setGuestId(Integer.parseInt(request.getParameter("guestId")));
                booking.setRoomId(Integer.parseInt(request.getParameter("roomId")));
                booking.setCheckIn(LocalDate.parse(request.getParameter("checkIn")));
                booking.setCheckOut(LocalDate.parse(request.getParameter("checkOut")));
                bookingDAO.create(booking);
                redirect(response, request, "Booking created successfully.", null);
            } else if ("checkin".equals(action)) {
                bookingDAO.updateStatus(Integer.parseInt(request.getParameter("id")), "CHECKED_IN");
                redirect(response, request, "Guest checked in successfully.", null);
            } else if ("checkout".equals(action)) {
                bookingDAO.updateStatus(Integer.parseInt(request.getParameter("id")), "CHECKED_OUT");
                redirect(response, request, "Guest checked out successfully.", null);
            } else if ("cancel".equals(action)) {
                bookingDAO.updateStatus(Integer.parseInt(request.getParameter("id")), "CANCELLED");
                redirect(response, request, "Booking cancelled successfully.", null);
            } else if ("delete".equals(action)) {
                bookingDAO.delete(Integer.parseInt(request.getParameter("id")));
                redirect(response, request, "Booking deleted successfully.", null);
            } else {
                redirect(response, request, null, "Invalid booking action.");
            }
        } catch (Exception e) {
            redirect(response, request, null, e.getMessage());
        }
    }

    private void redirect(HttpServletResponse response, HttpServletRequest request, String success, String error) throws IOException {
        String param = success != null ? "success=" + encode(success) : "error=" + encode(error);
        response.sendRedirect(request.getContextPath() + "/bookings?" + param);
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
