/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models.entity;

/**
 *
 * @author Nesh
 */
public class Notification {

    private NotificationType type;
    private Borrow borrow;

    public Notification(NotificationType type, Borrow borrow) {
        this.type = type;
        this.borrow = borrow;
    }

    public NotificationType getType() {
        return type;
    }

    public Borrow getBorrow() {
        return borrow;
    }
}
