using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PodBookingSystem.B.ServiceLayer.DTO.Request
{
    public class UpdateProfileRequest
    {
        public string? Name { get; set; }
        public string? Phone { get; set; }
        public string? Email { get; set; }
        public string? AvatarUrl { get; set; }
    }
}
