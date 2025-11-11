package com.example.g6podbookingsystem.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.activities.BookingDetailActivity;
import com.example.g6podbookingsystem.models.Booking;
import com.example.g6podbookingsystem.models.BookingDetail;
import com.example.g6podbookingsystem.repositories.BookingRepository;
import com.example.g6podbookingsystem.services.BookingApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    public BookingAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.bind(bookings.get(position));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvTime, tvStatus;
        Button btnCheckIn, btnCheckOut;
        ImageView imgAvatar;

        BookingApi bookingApi;
        ImageButton btnDelete;
        private SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            bookingApi = BookingRepository.getBookingService();
        }
        private Date parseDateTime(String dateString, String timeString) {
            if (dateString == null || timeString == null) {
                Log.e("BookingAdapter", "Date hoặc Time bị null");
                return null;
            }

            try {
                String cleanTime = timeString.split("\\.")[0];
                String fullDateTimeStr = dateString + " " + cleanTime;
                return fullDateTimeFormat.parse(fullDateTimeStr);
            } catch (ParseException e) {
                Log.e("BookingAdapter", "Lỗi parse ngày giờ: " + dateString + " " + timeString, e);
                return null;
            } catch (Exception e) {
                Log.e("BookingAdapter", "Lỗi không xác định khi parse: " + timeString, e);
                return null;
            }
        }

        public void bind(Booking b) {

            String customerName = (b.getUser() != null)
                    ? b.getUser().getName()
                    : "Khách #" + b.getUserId();

            String bookingDate = b.getBookingDate();
            String time = "N/A";
            String startTimeStr = null;
            String endTimeStr = null;
            if (b.getBookingDetails() != null && !b.getBookingDetails().isEmpty()) {
                BookingDetail d = b.getBookingDetails().get(0);
                time = d.getStartTime() + " - " + d.getEndTime();
                startTimeStr = d.getStartTime();
                endTimeStr = d.getEndTime();
            }

            if (b.getUser() != null && b.getUser().getAvatarUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(b.getUser().getAvatarUrl())
                        .placeholder(R.drawable.ic_account) // ảnh mặc định
                        .circleCrop()
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_account);
            }

            tvCustomer.setText(customerName + " (" + b.getBookingId() + ")");
            tvTime.setText(time);
            tvStatus.setText("Trạng thái: " + b.getStatus());

            switch (b.getStatus().toUpperCase()) {
                case "BOOKED":
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckOut.setVisibility(View.GONE);
                    break;

                case "CHECK-IN":
                    btnCheckIn.setVisibility(View.GONE);
                    btnCheckOut.setVisibility(View.VISIBLE);
                    break;

                case "CHECK-OUT":
                default:
                    btnCheckIn.setVisibility(View.GONE);
                    btnCheckOut.setVisibility(View.GONE);
                    break;
            }
            final String finalBookingDate = bookingDate;
            final String finalStartTime = startTimeStr;
            final String finalEndTime = endTimeStr;

            btnCheckIn.setOnClickListener(v -> {
                if (finalBookingDate == null || finalStartTime == null) {
                    Toast.makeText(itemView.getContext(), "Lỗi: Không tìm thấy thời gian booking", Toast.LENGTH_SHORT).show();
                    return;
                }
                Date startTime = parseDateTime(finalBookingDate, finalStartTime);
                Date endTime = parseDateTime(finalBookingDate, finalEndTime);
                Date now = new Date();

                if (startTime == null) {
                    Toast.makeText(itemView.getContext(), "Lỗi: Không thể đọc định dạng thời gian", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (endTime == null) {
                    Toast.makeText(itemView.getContext(), "Lỗi: Không thể đọc định dạng thời gian", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(now.after(endTime)){
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("⚠️ Xác nhận Check-In Trễ")
                            .setMessage("Đã quá hạn nhận phòng, vui lòng hủy đặt phòng")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }
                if (now.before(startTime)) {
                    Toast.makeText(itemView.getContext(), "Chưa đến giờ check-in. Vui lòng đợi.", Toast.LENGTH_LONG).show();
                    return;
                }

                bookingApi = BookingRepository.getBookingService();
                bookingApi.checkIn(b.getBookingId()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.isSuccessful() && response.body() != null && response.body() > 0) {
                            b.setStatus("CHECK-IN");
                            notifyItemChanged(getAdapterPosition());
                            Toast.makeText(itemView.getContext(), "✅ Check-in thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(itemView.getContext(), "check-in thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(itemView.getContext(), "Lỗi server", Toast.LENGTH_SHORT).show();
                    }
                });


            });

            btnCheckOut.setOnClickListener(v -> {
                if (finalBookingDate == null || finalEndTime == null) {
                    Toast.makeText(itemView.getContext(), "Lỗi: Không tìm thấy thời gian booking", Toast.LENGTH_SHORT).show();
                    return;
                }

                Date endTime = parseDateTime(finalBookingDate, finalEndTime);
                Date now = new Date();
                boolean isOvertime = false;

                if (endTime == null) {
                    Toast.makeText(itemView.getContext(), "Lỗi: Không thể đọc định dạng thời gian", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (now.after(endTime)) {

                    long diffInMillis = now.getTime() - endTime.getTime();
                    long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
                    String timeLateFormatted;
                    if (diffInMinutes >= 60) {
                        long hours = diffInMinutes / 60;
                        long minutes = diffInMinutes % 60;
                        timeLateFormatted = hours + " giờ " + minutes + " phút";
                    } else {
                        timeLateFormatted = diffInMinutes + " phút";
                    }
                    String overtimeMsg = "Check-out trễ " + timeLateFormatted + ". Sẽ tính phụ phí.";
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("⚠️ Xác nhận Check-out Trễ")
                            .setMessage(overtimeMsg + "\n\nBạn có chắc chắn muốn check-out?")
                            .setPositiveButton("Check-out", (dialog, which) -> {
                                proceedWithCheckOutApi(b, true);
                            })
                            .setNegativeButton("Hủy", null)
                            .show();

                } else {
                    proceedWithCheckOutApi(b, false); // false = không overtime
                }
//                bookingApi = BookingRepository.getBookingService();
//                bookingApi.checkOut(b.getBookingId()).enqueue(new Callback<Integer>() {
//                    @Override
//                    public void onResponse(Call<Integer> call, Response<Integer> response) {
//                        if (response.isSuccessful() && response.body() != null && response.body() > 0) {
//                            b.setStatus("CHECK-OUT");
//                            notifyItemChanged(getAdapterPosition());
//                            Toast.makeText(itemView.getContext(), "✅ Check-out thành công", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(itemView.getContext(), "check-out thất bại", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Integer> call, Throwable t) {
//                        Toast.makeText(itemView.getContext(), "Lỗi server", Toast.LENGTH_SHORT).show();
//                    }
//                });
            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), BookingDetailActivity.class);
                intent.putExtra("bookingId", b.getBookingId());
                itemView.getContext().startActivity(intent);
            });

            if (btnDelete != null) {
                btnDelete.setOnClickListener(v -> {
                    // Hỏi xác nhận trước khi xóa
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa Booking #" + b.getBookingId() + "?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                // Gọi API Delete
                                deleteBookingApi(b.getBookingId());
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                });
            }
        }

        private void proceedWithCheckOutApi(Booking b, boolean isOvertime) {
            if (bookingApi == null) {
                bookingApi = BookingRepository.getBookingService();
            }

            bookingApi.checkOut(b.getBookingId()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() && response.body() != null && response.body() > 0) {
                        b.setStatus("CHECK-OUT");
                        notifyItemChanged(getAdapterPosition());
                        if (!isOvertime) {
                            Toast.makeText(itemView.getContext(), "✅ Check-out thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(itemView.getContext(), "✅ Đã xác nhận check-out trễ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(itemView.getContext(), "check-out thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Lỗi server", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // --- THÊM HÀM GỌI API DELETE ---
        private void deleteBookingApi(int bookingId) {
            bookingApi.deleteBooking(bookingId).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null && response.body() == true) {
                        Toast.makeText(itemView.getContext(), "Đã xóa Booking #" + bookingId, Toast.LENGTH_SHORT).show();

                        // Xóa item khỏi danh sách và cập nhật RecyclerView
                        int currentPosition = getAdapterPosition();
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            bookings.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            notifyItemRangeChanged(currentPosition, bookings.size());
                        }
                    } else {
                        Toast.makeText(itemView.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("BookingAdapter", "Delete failed: " + response.message());

                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("BookingAdapter", "Delete network error: " + t.getMessage());
                }
            });
        }
    }
}
