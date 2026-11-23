package com.example.demov1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
//Annotation	Công dụng	Bắt buộc?
//@Entity	Đánh dấu class là bảng	Có
//@PrimaryKey	Xác định khóa chính	Có, ít nhất một khóa chính
//@ColumnInfo	Tùy chỉnh tên cột	Không
//@Ignore	Không lưu thuộc tính vào database	Không
//@Embedded	Nhúng các thuộc tính của object khác	Không
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0 ,
    val title: String,
    val category: String,
    val amount: Double,
    val type: String,
    val date: String,
    val imageUrl: String
)
