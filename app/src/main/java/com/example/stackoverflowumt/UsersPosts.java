package com.example.stackoverflowumt;

public class UsersPosts {

    String Post_Title, Post_Description, Post_UserFullName, Post_Username, Post_Tags, Post_UID, Post_IsResolved;

    public UsersPosts() {
    }

    public UsersPosts(String post_Title, String post_Description, String post_UserFullName, String post_Username, String post_Tags, String post_UID, String post_IsResolved) {
        Post_Title = post_Title;
        Post_Description = post_Description;
        Post_UserFullName = post_UserFullName;
        Post_Username = post_Username;
        Post_Tags = post_Tags;
        Post_UID = post_UID;
        Post_IsResolved = post_IsResolved;
    }

    @Override
    public String toString() {
        return "UsersPosts{" +
                "Post_Title='" + Post_Title + '\'' +
                ", Post_Description='" + Post_Description + '\'' +
                ", Post_UserFullName='" + Post_UserFullName + '\'' +
                ", Post_Username='" + Post_Username + '\'' +
                ", Post_Tags='" + Post_Tags + '\'' +
                ", Post_UID='" + Post_UID + '\'' +
                ", Post_IsResolved='" + Post_IsResolved + '\'' +
                '}';
    }

    public String getPost_Title() {
        return Post_Title;
    }

    public void setPost_Title(String post_Title) {
        Post_Title = post_Title;
    }

    public String getPost_Description() {
        return Post_Description;
    }

    public void setPost_Description(String post_Description) {
        Post_Description = post_Description;
    }

    public String getPost_UserFullName() {
        return Post_UserFullName;
    }

    public void setPost_UserFullName(String post_UserFullName) {
        Post_UserFullName = post_UserFullName;
    }

    public String getPost_Username() {
        return Post_Username;
    }

    public void setPost_Username(String post_Username) {
        Post_Username = post_Username;
    }

    public String getPost_Tags() {
        return Post_Tags;
    }

    public void setPost_Tags(String post_Tags) {
        Post_Tags = post_Tags;
    }

    public String getPost_UID() {
        return Post_UID;
    }

    public void setPost_UID(String post_UID) {
        Post_UID = post_UID;
    }

    public String getPost_IsResolved() {
        return Post_IsResolved;
    }

    public void setPost_IsResolved(String post_IsResolved) {
        Post_IsResolved = post_IsResolved;
    }
}
