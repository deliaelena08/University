package com.example.demo.SocialNetwork.Repos.DB;
import com.example.demo.SocialNetwork.Model.Message;
import com.example.demo.SocialNetwork.Model.User;
import com.example.demo.SocialNetwork.Repos.Repository;
import com.example.demo.SocialNetwork.Repos.Memo.RepositoryMessage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositoryMessagesDB implements RepositoryMessage {
    private final String url;
    private final String username;
    private final String password;
    private final Repository<Long, User> repo;

    public RepositoryMessagesDB(String url, String username, String password, Repository<Long, User> repo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.repo = repo;
    }
    private Message createMessage(ResultSet rs) throws SQLException {
        Long messageID = rs.getLong("id_message");
        Long idsender = rs.getLong("sender");
        Long idreceiver = rs.getLong("receiver");
        String messageText = rs.getString("message");
        LocalDateTime date = rs.getTimestamp("date_message").toLocalDateTime();
        Optional<User> user1=repo.find(idsender);
        Optional<User> user2=repo.find(idreceiver);
        Message message=new Message(messageText,user1.get(),user2.get(),date);
        message.setId(messageID);
        return message;
    }
    @Override
    public Optional<Message> find(Long aLong) {
        String sql = "SELECT * FROM messages WHERE id_message = ?";
        try(Connection connection= DriverManager.getConnection(url,username,password); PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, aLong);

            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                Message message=createMessage(resultSet);
                return Optional.of(message);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a gasit mesajul");
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages=new ArrayList<>();
        String sql = "SELECT * FROM messages";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery()){
            while(resultSet.next()){
                Message request=createMessage(resultSet);
                messages.add(request);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a gasit cereri in baza de date");
        }
        return messages;
    }


    @Override
    public Iterable<Message> findChat(Long sender, Long receiver) {
        List<Message> messages=new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?)  ORDER BY date_message ASC ";
        try(Connection connection=DriverManager.getConnection(url,username,password);
        PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, sender);
            statement.setLong(2, receiver);
            statement.setLong(3, receiver);
            statement.setLong(4, sender);
            ResultSet resultSet=statement.executeQuery();
            while(resultSet.next()){
                Message request=createMessage(resultSet);
                messages.add(request);
            }
        } catch (SQLException e){
            System.out.println("Nu s-a gasit conversatie in baza de date");
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) {
        String sql="INSERT INTO messages(sender, receiver, message,date_message) VALUES (?,?,?,?)";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setLong(1, entity.getSender().getId());
            statement.setLong(2, entity.getReceiver().getId());
            statement.setString(3, entity.getMessage());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getTime()));
            int rows=statement.executeUpdate();
            if(rows>0){
                return Optional.of(entity);
            }
        }catch (SQLException e) {
            System.out.println("Nu s-a putut salva: "+e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        Optional<Message> request=find(aLong);
        if(request.isPresent()){
            String sql="DELETE FROM messages WHERE id_message = ?";
            try(Connection connection=DriverManager.getConnection(url,username,password);
                PreparedStatement statement=connection.prepareStatement(sql)){
                statement.setLong(1, request.get().getId());
                int rows=statement.executeUpdate();
                if(rows>0){
                    return request;
                }
            } catch (SQLException e){
                System.out.println("Nu s-a putut sterge: "+e.getMessage());
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        String sql="UPDATE messages SET message = ?, date_message = ? WHERE id_message = ?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, entity.getMessage());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getTime()));
            statement.setLong(3, entity.getId());
            int rows=statement.executeUpdate();
            if(rows>0){
                return Optional.of(entity);
            }
        }catch(SQLException e){
            System.out.println("Nu s-a putut face update: "+e.getMessage());
        }
        return Optional.empty();
    }
}
