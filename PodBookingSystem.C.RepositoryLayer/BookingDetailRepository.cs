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
    public class BookingDetailRepository
    {
        private readonly PodBookingSystemContext _context;
        public BookingDetailRepository(PodBookingSystemContext context)
        {
            _context = context;
        }
        public async Task<List<BookingDetail>> GetBookingDetailsAsync() => await _context.BookingDetails
            .Include(b => b.Room).ThenInclude(b => b.Type)
            .OrderByDescending(b => b.Timestamp)
            .ToListAsync();

        public async Task<BookingDetail> GetByIdAsync(int bookingDetailId)
            => await _context.BookingDetails
            .Include(b => b.Room).ThenInclude(b => b.Type)
            .AsNoTracking()
            .FirstOrDefaultAsync(b => b.BookingDetailId == bookingDetailId);
        public async Task<BookingDetail> GetByIdAsyncForUpdate(int bookingDetailId)
            => await _context.BookingDetails
            .Include(b => b.Room).ThenInclude(b => b.Type)
            .FirstOrDefaultAsync(b => b.BookingDetailId == bookingDetailId);
        public async Task<int> CreateAsync(BookingDetail bookingDetail)
        {
            _context.BookingDetails.Add(bookingDetail);
            return await _context.SaveChangesAsync();
        }
        public async Task<int> UpdateAsync(BookingDetail bookingDetail)
        {
            _context.BookingDetails.Update(bookingDetail);
            return await _context.SaveChangesAsync();
        }
        public async Task<bool> DeleteAsync(BookingDetail bookingDetail)
        {

            _context.BookingDetails.Remove(bookingDetail);
            await _context.SaveChangesAsync();
            return true;


        }
        public async Task<List<Booking>> GetBookingsByUserAsync(int userId)
        {
            return await _context.Bookings
                .Where(b => b.UserId == userId)
                .Include(b => b.BookingDetails)
                    .ThenInclude(d => d.Room)
                        .ThenInclude(r => r.Type)
                .Include(b => b.Payment)
                .Include(b => b.Feedback)
                .OrderByDescending(b => b.BookingDate)
                .ToListAsync();
        }
    }
}
