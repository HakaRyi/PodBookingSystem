using FirebaseAdmin.Auth;
using Microsoft.AspNetCore.Http;

namespace PodBookingSystem.B.ServiceLayer.Helpers
{
    public class FirebaseTokenHelper
    {
        public static async Task<FirebaseToken?> VerifyFirebaseTokenAsync(HttpRequest request)
        {
            var authHeader = request.Headers["Authorization"].ToString();
            if (string.IsNullOrEmpty(authHeader) || !authHeader.StartsWith("Bearer "))
                return null;

            var firebaseToken = authHeader.Substring("Bearer ".Length).Trim();
            try
            {
                return await FirebaseAuth.DefaultInstance.VerifyIdTokenAsync(firebaseToken);
            }
            catch
            {
                return null;
            }
        }
    }
}
