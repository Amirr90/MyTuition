package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class SubjectModel {

    public static DiffUtil.ItemCallback<SubjectModel> itemCallback = new DiffUtil.ItemCallback<SubjectModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull SubjectModel oldItem, @NonNull SubjectModel newItem) {
            return oldItem.getSubjectName().equals(newItem.getSubjectName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SubjectModel oldItem, @NonNull SubjectModel newItem) {
            return oldItem.equals(newItem);
        }
    };
    String image;
    String subjectName;
    String teachers;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectModel that = (SubjectModel) o;
        return Objects.equals(image, that.image) &&
                Objects.equals(subjectName, that.subjectName) &&
                Objects.equals(teachers, that.teachers) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, subjectName, teachers, id);
    }
}
