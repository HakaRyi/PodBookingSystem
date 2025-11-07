package com.example.g6podbookingsystem.fragments.admin_fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.g6podbookingsystem.R;
import com.example.g6podbookingsystem.models.Room;
import com.example.g6podbookingsystem.services.ApiClient;
import com.example.g6podbookingsystem.services.RoomApi;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailFragment extends Fragment {

    private static final String TAG = "RoomDetailFragment";

    private RoomApi roomApi;
    private Room currentRoom;
    private boolean isEditMode = false;

    private TextInputEditText etName, etCapacity, etTypeId, etPrice, etPriceDay, etDescription, etImgUrl;
    private Button btnSave, btnDelete;
    private MaterialToolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_detail, container, false);

        // Lấy đối tượng Room từ Bundle (nếu có)
        if (getArguments() != null && getArguments().containsKey("room")) {
            currentRoom = getArguments().getParcelable("room");
            isEditMode = true;
        } else {
            currentRoom = new Room(); // Tạo mới
            isEditMode = false;
        }

        // Khởi tạo service
        roomApi = ApiClient.getRoomApi();

        // Bind Views
        bindViews(view);

        // Cấu hình giao diện
        setupUI();

        return view;
    }

    private void bindViews(View view) {
        etName = view.findViewById(R.id.et_room_name);
        etCapacity = view.findViewById(R.id.et_room_capacity);
        etTypeId = view.findViewById(R.id.et_room_type_id);
        etPrice = view.findViewById(R.id.et_room_price);
        etPriceDay = view.findViewById(R.id.et_room_price_day);
        etDescription = view.findViewById(R.id.et_room_description);
        etImgUrl = view.findViewById(R.id.et_room_img_url);
        btnSave = view.findViewById(R.id.btn_save_room);
        btnDelete = view.findViewById(R.id.btn_delete_room);
        toolbar = view.findViewById(R.id.toolbar_detail);
    }

    private void setupUI() {
        // Nút back
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (isEditMode) {
            toolbar.setTitle("Sửa thông tin Phòng");
            populateFields();
            btnSave.setText("Lưu Cập Nhật");
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            toolbar.setTitle("Tạo Phòng Mới");
            btnSave.setText("Tạo Phòng");
            btnDelete.setVisibility(View.GONE);
        }

        // Click Listener
        btnSave.setOnClickListener(v -> saveRoom());
        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
    }

    // Đổ dữ liệu lên form nếu là chế độ Sửa
    private void populateFields() {
        if (currentRoom == null) return;
        etName.setText(currentRoom.getName());
        etCapacity.setText(String.valueOf(currentRoom.getCapacity()));
        etTypeId.setText(String.valueOf(currentRoom.getTypeId()));
        etPrice.setText(String.valueOf(currentRoom.getPrice()));
        etPriceDay.setText(String.valueOf(currentRoom.getPriceDay()));
        etDescription.setText(currentRoom.getDescription());
        etImgUrl.setText(currentRoom.getImgUrl());
    }

    // Lấy dữ liệu từ form
    private boolean getFields() {
        try {
            currentRoom.setName(etName.getText().toString().trim());
            currentRoom.setCapacity(Integer.parseInt(etCapacity.getText().toString()));
            currentRoom.setTypeId(Integer.parseInt(etTypeId.getText().toString()));
            currentRoom.setPrice(Double.parseDouble(etPrice.getText().toString()));
            currentRoom.setPriceDay(Double.parseDouble(etPriceDay.getText().toString()));
            currentRoom.setDescription(etDescription.getText().toString().trim());
            currentRoom.setImgUrl(etImgUrl.getText().toString().trim());
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Vui lòng nhập đúng định dạng số", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void saveRoom() {
        if (!getFields()) {
            return; // Dữ liệu không hợp lệ
        }

        if (isEditMode) {
            // ----- CHẾ ĐỘ SỬA (PUT) -----
            // Lưu ý: Backend của bạn cần roomId trong cả URL và Body
            roomApi.updateRoom(currentRoom.getRoomId(), currentRoom).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() && response.body() != null && response.body() == 1) {
                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        // Quay lại danh sách
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Log.e(TAG, "Update failed: " + response.code() + " - " + response.message());
                        Toast.makeText(getContext(), "Cập nhật thất bại. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "Update API call failed: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // ----- CHẾ ĐỘ TẠO MỚI (POST) -----
            // (Backend của bạn sẽ tự gán Status = "AVAILABLE")
            roomApi.createRoom(currentRoom).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() && response.body() != null && response.body() == 1) {
                        Toast.makeText(getContext(), "Tạo phòng mới thành công!", Toast.LENGTH_SHORT).show();
                        // Quay lại danh sách
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Log.e(TAG, "Create failed: " + response.code() + " - " + response.message());
                        Toast.makeText(getContext(), "Tạo mới thất bại. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "Create API call failed: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận Xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng '" + currentRoom.getName() + "' không?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Xóa", (dialog, which) -> deleteRoom())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteRoom() {
        if (!isEditMode || currentRoom == null) return;

        // ----- CHẾ ĐỘ XÓA (DELETE) -----
        roomApi.deleteRoom(currentRoom.getRoomId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Backend trả về 204 No Content là thành công
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Xóa phòng thành công!", Toast.LENGTH_SHORT).show();
                    // Quay lại danh sách
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Log.e(TAG, "Delete failed: " + response.code() + " - " + response.message());
                    Toast.makeText(getContext(), "Xóa thất bại. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Delete API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
