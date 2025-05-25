import decorator.DelayTaskRunner;
import decorator.StrategyTaskRunner;
import factory.Strategy;
import model.MessageTask;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MessageTask m1 = new MessageTask("1", "Sărbătorește cu stil!", "🎉 Începe săptămâna cu un zâmbet!", "Ziua1", "HappyTeam", LocalDateTime.now().plusHours(1));
        MessageTask m2 = new MessageTask("2", "Recompensă de Crăciun", "🎄 Ai câștigat un cadou surpriză!", "Ziua2", "Gigi", LocalDateTime.now().plusHours(2));
        MessageTask m3 = new MessageTask("3", "Ziua Internațională a Muncii", "💼 Începe săptămâna cu energie!", "Ziua3", "Ana", LocalDateTime.now().plusHours(3));
        MessageTask m4 = new MessageTask("4", "Măsură de succes!", "🏆 Ești mai aproape de obiectivele tale!", "Ziua4", "Beni", LocalDateTime.now().plusHours(4));
        MessageTask m5 = new MessageTask("5", "Fii creativ", "🌟 Ai completat sarcina cu brio!", "Ziua5", "Carla", LocalDateTime.now().plusHours(5));

        ArrayList<MessageTask> listMesaje = new ArrayList<>();
        listMesaje.add(m1);
        listMesaje.add(m2);
        listMesaje.add(m3);
        listMesaje.add(m4);
        listMesaje.add(m5);

        for (MessageTask m : listMesaje) {
            System.out.println(m);
        }

        StrategyTaskRunner st = new StrategyTaskRunner(Strategy.valueOf(args[0]));
        st.addTask(m1);
        st.addTask(m2);
        st.addTask(m3);
        st.addTask(m4);
        st.addTask(m5);
        st.executeOneTask();
        // 2. Decorator DelayTaskRunner
        System.out.println("\nExecutare cu DelayTaskRunner:");
        DelayTaskRunner delayRunner = new DelayTaskRunner(st);  // Decorarea StrategyTaskRunner cu DelayTaskRunner
        for (MessageTask task : listMesaje) {
            delayRunner.addTask(task);
        }
        delayRunner.executeOneTask();
    }
}
