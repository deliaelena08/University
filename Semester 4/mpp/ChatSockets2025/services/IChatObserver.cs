using chat.model;

namespace chat.services;

public interface IChatObserver
{
    void messageReceived(Message message);
    void friendLoggedIn(User friend);
    void friendLoggedOut(User friend);

}