package com.horrorcore.repositories;

import com.horrorcore.models.Comment;
import com.horrorcore.models.Post;
import com.horrorcore.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPostRepository implements PostRepository{
    private final Connection connection;

    public JDBCPostRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Post save(Post post) {
        final String sql = "INSERT INTO posts (status,username) VALUE (?,?)";
        final String[] returnColumns = {"id", "likes", "is_draft", "dislikes", "created_at", "updated_at"};
        try (PreparedStatement statement = connection.prepareStatement(sql, returnColumns)) {
            statement.setString(1, post.getStatus());
            statement.setString(2, post.getUsername());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    post.setId(resultSet.getLong("id"));
                    post.setDraft(resultSet.getBoolean("is_draft"));
                    post.setLikes(resultSet.getInt("likes"));
                    post.setDislikes(resultSet.getInt("dislikes"));
                    post.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                    post.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return post;
    }

    @Override
    public List<Post> findAll() {
        final String sql = "SELECT * FROM posts";
        List<Post> posts = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                Post post = new Post();
                long id = resultSet.getLong("id");
                post.setId(id);
                post.setStatus(resultSet.getString("status"));
                post.setUsername(resultSet.getString("username"));
                post.setDraft(resultSet.getBoolean("is_draft"));
                post.setLikes(resultSet.getInt("likes"));
                post.setDislikes(resultSet.getInt("dislikes"));
                post.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                post.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
                post.setComments(findCommentByPostId(id));
                posts.add(post);
                post = null;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return posts;
    }

    @Override
    public List<Post> findAllByUsername(String username) {
        return List.of();
    }

    @Override
    public Optional<Post> findById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(long id) {
        return false;
    }

    @Override
    public boolean updatePost(Post post) {
        return false;
    }

    private List<Comment> findCommentByPostId(long id) {
        final String sql = "SELECT * FROM comments WHERE post_id = ?";
        List<Comment> comments = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setId(resultSet.getLong("id"));
                    comment.setMessage(resultSet.getString("message"));
                    comment.setPostDate(resultSet.getTimestamp("post_date").toLocalDateTime());
                    comments.add(comment);
                    comment = null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred when getting comments");
        }
        return comments;
    }
}
