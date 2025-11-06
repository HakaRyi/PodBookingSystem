using FirebaseAdmin.Auth;
using Microsoft.AspNetCore.Http;

namespace PodBookingSystem.B.ServiceLayer.Helpers
{
    public class FirebaseTokenHelper
    {
        public static async Task<FirebaseToken?> VerifyFirebaseTokenAsync(HttpRequest request)
        {
            var authHeader = request.Headers["Authorization"].ToString();
            Console.WriteLine($"[DEBUG] Authorization Header: {authHeader}");
            if (string.IsNullOrEmpty(authHeader) || !authHeader.StartsWith("Bearer "))
                return null;

            var firebaseToken = authHeader.Substring("Bearer ".Length).Trim();
            Console.WriteLine($"[DEBUG] Firebase Token: {firebaseToken.Substring(0, 20)}...");
            try
            {
                var decoded = await FirebaseAuth.DefaultInstance.VerifyIdTokenAsync(firebaseToken);
                Console.WriteLine($"[DEBUG] Token hợp lệ! Email: {decoded.Claims.GetValueOrDefault("email")}");
                return decoded;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[ERROR] Verify token failed: {ex.Message}");
                return null;
            }
        }
    }
}
