package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static Comment mapToComment(CommentDto commentDto, long itemId, long commentatorId) {
        Comment comment = new Comment();
        Item item = new Item();
        User commentator = new User();
        item.setId(itemId);
        commentator.setId(commentatorId);
        comment.setItem(item);
        comment.setAuthor(commentator);
        comment.setText(commentDto.getText());
        return comment;
    }

    public static CommentDto mapCommentToResponse(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }
}
