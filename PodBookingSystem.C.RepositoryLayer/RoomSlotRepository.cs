using Microsoft.EntityFrameworkCore;
using PodBookingSystem.C.RepositoryLayer.DBContext;
using PodBookingSystem.C.RepositoryLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.C.RepositoryLayer
{
    public class RoomSlotRepository
    {
        private readonly PodBookingSystemContext _context;

        public RoomSlotRepository(PodBookingSystemContext context)
        {
            _context = context;
        }

        public async Task<List<RoomSlot>> GetAllAsync()
        {
            return await _context.RoomSlots
                .Include(rs => rs.Room)
                .Include(rs => rs.Slot)
                .Include(rs => rs.Booking)
                .ToListAsync();
        }

        public async Task<RoomSlot> GetByIdAsync(int roomId, int slotId)
        {
            return await _context.RoomSlots
                .Include(rs => rs.Room)
                .Include(rs => rs.Slot)
                .Include(rs => rs.Booking)
                .FirstOrDefaultAsync(rs => rs.RoomId == roomId && rs.SlotId == slotId);
        }

        public async Task<int> CreateAsync(RoomSlot roomSlot)
        {
            await _context.RoomSlots.AddAsync(roomSlot);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> UpdateAsync(RoomSlot roomSlot)
        {
            _context.RoomSlots.Update(roomSlot);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> DeleteAsync(int roomId, int slotId)
        {
            var roomSlot = await GetByIdAsync(roomId, slotId);
            if (roomSlot != null)
            {
                _context.RoomSlots.Remove(roomSlot);
                return await _context.SaveChangesAsync();
            }
            return 0;
        }
    }
}
