package test.connect.geoexploreapp;

import test.connect.geoexploreapp.model.Comment;

public interface CommentActionListener {
    void onDeleteComment(Long commentId, int position);
    void onEditComment(Comment comment, String newComment);
}
