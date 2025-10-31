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
    public class RoomRepository
    {
        private readonly PodBookingSystemContext context;
        public RoomRepository(PodBookingSystemContext context)
        {
            this.context = context;
        }
        public async Task<List<Room>> GetRoomsAsync()
        {
            return await context.Rooms
                .Include(r => r.Type)
                .OrderByDescending(r => r.RoomId)
                .ToListAsync();
        }
        public async Task<Room> GetRoomAsync(int id)
        {
            return await context.Rooms
               .Include(r => r.Type)
               .FirstOrDefaultAsync(r => r.RoomId == id);
        }
        public async Task<int> CretaeAsync(Room room)
        {
            context.Rooms.Add(room);
            return await context.SaveChangesAsync();
        }
        public async Task<int> UpdateAsync(Room room)
        {
            context.Rooms.Update(room);
            return await context.SaveChangesAsync();
        }
        public async Task<bool> DeleteAsync(int id)
        {
            var room = await GetRoomAsync(id);
            if (room != null)
            {
                context.Rooms.Remove(room);
                await context.SaveChangesAsync();
                return true;
            }
            return false;

        }
    }
}
