using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using PodBookingSystem.C.RepositoryLayer.DBContext;
using PodBookingSystem.C.RepositoryLayer.Models;

namespace PodBookingSystem.C.RepositoryLayer
{
    public class BookingRepository
    {
        private readonly PodBookingSystemContext _context;
        public BookingRepository(PodBookingSystemContext context)
        {
            _context = context;
        }
        public async Task<List<Booking>> GetBookingsAsync() => await _context.Bookings
            .Include(b => b.BookingDetails)
            .Include(b => b.User)
            .Include(b => b.RoomSlots)
            .Include(b => b.Payment)
            .Include(b => b.Feedback)
            .OrderByDescending(b => b.BookingDate)
            .ToListAsync();
        public async Task<List<Booking>> GetBookingsByBOOKEDAsync() => await _context.Bookings
            .Include(b => b.BookingDetails)
            .Include(b => b.User)
            .Include(b => b.RoomSlots)
            .Include(b => b.Payment)
            .Include(b => b.Feedback)
            .Where(b => b.Status == "BOOKED")
            .OrderByDescending(b => b.BookingDate)
            .ToListAsync();
        public async Task<List<Booking>> GetBookingsByAVAILABLEAsync() => await _context.Bookings
            .Include(b => b.BookingDetails)
            .Include(b => b.User)
            .Include(b => b.RoomSlots)
            .Include(b => b.Payment)
            .Include(b => b.Feedback)
            .Where(b => b.Status == "AVAILABLE")
            .OrderByDescending(b => b.BookingDate)
            .ToListAsync();
        public async Task<List<Booking>> GetBookingsByDONEAsync() => await _context.Bookings
            .Include(b => b.BookingDetails)
            .Include(b => b.User)
            .Include(b => b.RoomSlots)
            .Include(b => b.Payment)
            .Include(b => b.Feedback)
            .Where(b => b.Status == "DONE")
            .OrderByDescending(b => b.BookingDate)
            .ToListAsync();
        public async Task<List<Booking>> GetBookingsByCHECKINAsync() => await _context.Bookings
            .Include(b => b.BookingDetails)
            .Include(b => b.User)
            .Include(b => b.RoomSlots)
            .Include(b => b.Payment)
            .Include(b => b.Feedback)
            .Where(b => b.Status == "CHECK-IN")
            .OrderByDescending(b => b.BookingDate)
            .ToListAsync();
        public async Task<List<Booking>> GetBookingsByCHECKOUTAsync() => await _context.Bookings
            .Include(b => b.BookingDetails)
            .Include(b => b.User)
            .Include(b => b.RoomSlots)
            .Include(b => b.Payment)
            .Include(b => b.Feedback)
            .Where(b => b.Status == "CHECK-OUT")
            .OrderByDescending(b => b.BookingDate)
            .ToListAsync();

        public async Task<Booking> GetByIdAsync(int bookingId) 
            => await _context.Bookings
            .Include(b => b.BookingDetails)
            .Include(b => b.User)
            .Include(b => b.RoomSlots)
            .Include(b => b.Payment)
            .Include(b => b.Feedback)
            .FirstOrDefaultAsync(b => b.BookingId == bookingId);
        public async Task<int> CretaeAsync(Booking booking)
        {
            _context.Bookings.Add(booking);
            return await _context.SaveChangesAsync();
        }
        public async Task<int> UpdateAsync(Booking booking)
        {
            _context.Bookings.Update(booking);
            return await _context.SaveChangesAsync();
        }
        public async Task<bool> DeleteAsync(int id)
        {
            var booking = await GetByIdAsync(id);
            if(booking != null)
            {
                _context.Bookings.Remove(booking);
                await _context.SaveChangesAsync();
                return true;
            }
            return false;

        }
    }
}
