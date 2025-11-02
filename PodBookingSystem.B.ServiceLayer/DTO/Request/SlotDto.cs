using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer.DTO.Request
{
    public class SlotDto
    {
        public int SlotId { get; set; }
        public DateOnly BookingDate { get; set; }
    }
}
