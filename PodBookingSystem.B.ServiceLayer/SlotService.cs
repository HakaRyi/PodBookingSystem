using PodBookingSystem.C.RepositoryLayer.Models;
using PodBookingSystem.C.RepositoryLayer;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PodBookingSystem.B.ServiceLayer.DTO.Response;
using PodBookingSystem.B.ServiceLayer.DTO.Request;

namespace PodBookingSystem.B.ServiceLayer
{
    public class SlotService
    {
        private readonly SlotRepository _slotRepository;

        public SlotService(SlotRepository slotRepository)
        {
            _slotRepository = slotRepository;
        }

        public async Task<List<SlotResponse>> GetAllAsync()
        {
            var slots = await _slotRepository.GetAllAsync();
            return slots.Select(s => new SlotResponse
            {
                SlotId = s.SlotId,
                Description = s.Description
            }).ToList();
        }

        public async Task<SlotResponse> GetByIdAsync(int id)
        {
            var slot = await _slotRepository.GetByIdAsync(id);
            if (slot == null) return null;

            return new SlotResponse
            {
                SlotId = slot.SlotId,
                Description = slot.Description
            };
        }

        public async Task<int> CreateAsync(SlotRequest dto)
        {
            var slot = new Slot
            {
                Description = dto.Description
            };
            return await _slotRepository.CreateAsync(slot);
        }

        public async Task<int> UpdateAsync(int id, SlotRequest dto)
        {
            var existing = await _slotRepository.GetByIdAsync(id);
            if (existing == null) return 0;

            existing.Description = dto.Description;
            return await _slotRepository.UpdateAsync(existing);
        }

        public async Task<int> DeleteAsync(int id)
        {
            return await _slotRepository.DeleteAsync(id);
        }
    }
}
