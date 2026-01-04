package org.example.restaurant.Services;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.example.restaurant.Repos.IRepository;
import org.example.restaurant.Repos.OrderItemsRepository;
import org.example.restaurant.models.*;

public class Service {
    IRepository<Integer, Table> tableRepository;
    IRepository<Integer, MenuItem> menuItemRepository;
    IRepository<Integer, Order> orderRepository;
    OrderItemsRepository orderItemsRepository;

    public Service(IRepository<Integer, Table> tableRepository, IRepository<Integer, MenuItem> menuItemRepository,
                   IRepository<Integer, Order> orderRepository, OrderItemsRepository orderItemsRepository) {
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    public void addTable(Table table) {
        tableRepository.save(table);
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
    }

    public void addOrder(Order order) {
        orderRepository.save(order);
    }

    public void addOrderItems(OrderItems orderItems) {
        orderItemsRepository.save(orderItems);
    }

    public Iterable<Table> getTables() {
        return tableRepository.findAll();
    }

    public Iterable<MenuItem> getMenuItems() {
        return menuItemRepository.findAll();
    }

    public Iterable<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Iterable<OrderItems> getOrderItems() {
        return orderItemsRepository.findAll();
    }

    public Table getTable(int id) {
        return tableRepository.find(id).get();
    }

    public List<MenuItem> getMenuItemsForOrder(int orderId) {
        return StreamSupport.stream(orderItemsRepository.findAll().spliterator(), false)
                .filter(orderItem -> orderItem.getOrderId() == orderId)
                .map(orderItem -> menuItemRepository.find(orderItem.getMenuItemId()).get())
                .collect(Collectors.toList());
    }

    public Map<String, List<MenuItem>> groupMenuByCategory(Iterable<MenuItem> menuItems) {
        return StreamSupport.stream(menuItems.spliterator(), false)
                .collect(Collectors.groupingBy(MenuItem::getCategory));
    }

    public void placeOrder(int tableId, List<MenuItem> selectedMenuItems) {
        Order newOrder = new Order(tableId, LocalDateTime.now(), Status.PLACED);
        Order savedOrder = orderRepository.save(newOrder).get();
        for (MenuItem menuItem : selectedMenuItems) {
            OrderItems orderItem = new OrderItems(savedOrder.getId(), menuItem.getId());
            orderItemsRepository.save(orderItem);
        }
        notifyOrderPlaced();
    }
    private Runnable onOrderPlacedCallback;
    private Consumer<Order> onOrderStatusChangedCallback;

    public void setOnOrderPlacedCallback(Runnable callback) {
        this.onOrderPlacedCallback = callback;
    }

    public void setOnOrderStatusChangedCallback(Consumer<Order> callback) {
        this.onOrderStatusChangedCallback = callback;
    }

    public void notifyOrderPlaced() {
        if (onOrderPlacedCallback != null) {
            onOrderPlacedCallback.run();
        }
    }

    public void notifyOrderStatusChanged(Order order) {
        if (onOrderStatusChangedCallback != null) {
            onOrderStatusChangedCallback.accept(order);
        }
    }



    public List<Order> getPlacedOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> order.getStatus() == Status.PLACED)
                .sorted(Comparator.comparing(Order::getDate))
                .collect(Collectors.toList());
    }
    public void updateOrderStatus(int orderId, Status newStatus) {
        Order order = orderRepository.find(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(newStatus);
        orderRepository.update(order);
        notifyOrderStatusChanged(order);
    }
    public List<Order> getPreparingOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> order.getStatus() == Status.PREPARING)
                .sorted(Comparator.comparing(Order::getDate))
                .collect(Collectors.toList());
    }

}
