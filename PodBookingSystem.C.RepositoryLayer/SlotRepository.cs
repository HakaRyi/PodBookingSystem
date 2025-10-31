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
    public class SlotRepository
    {
        private readonly PodBookingSystemContext _context;

        public SlotRepository(PodBookingSystemContext context)
        {
            _context = context;
        }

        public async Task<List<Slot>> GetAllAsync()
        {
            return await _context.Slots.ToListAsync();
        }

        public async Task<Slot> GetByIdAsync(int id)
        {
            return await _context.Slots.FirstOrDefaultAsync(s => s.SlotId == id);
        }

        public async Task<int> CreateAsync(Slot slot)
        {
            await _context.Slots.AddAsync(slot);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> UpdateAsync(Slot slot)
        {
            _context.Slots.Update(slot);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> DeleteAsync(int id)
        {
            var slot = await GetByIdAsync(id);
            if (slot != null)
            {
                _context.Slots.Remove(slot);
                return await _context.SaveChangesAsync();
            }
            return 0;
        }
    }
}
