package controllers;

/**
 * Třída (controller) starající se o refresh hlavního pohledu
 *
 * @author Petr Hejhal (hejhape1@fel.cvut.cz)
 */
public class RefreshController {

    public static RefreshController instance; // instance této třídy
    private BookTabController bookTabController; // Kontroler záložky knih hlavního pohledu
    private CustomerTabController customerTabController; // Kontroler záložky zákazníků hlavního pohledu
    private BorrowTabController borrowTabController; // Kontroler záložky půjček hlavního pohledu
    private NotificationTabController notificationTabController; // Kontroler záložky oznámení hlavního pohledu

    /**
     * SINGLETON - RefreshController
     *
     * @return instance této třídy
     */
    public static RefreshController getInstance() {
        synchronized (RefreshController.class) {
            if (instance == null) {
                instance = new RefreshController();
            }
        }
        return instance;
    }

    private RefreshController() {
        // SINGLETON
    }

    /**
     * Registrace kontroleru záložky knih hlavného pohledu
     *
     * @param bookTabController kontrolet záložky knih
     */
    public void setBookTabController(BookTabController bookTabController) {
        this.bookTabController = bookTabController;
    }

    /**
     * Registrace kontroleru záložky zákazníků hlavného pohledu
     *
     * @param customerTabController kontroler záložky zákazníků
     */
    public void setCustomerTabController(CustomerTabController customerTabController) {
        this.customerTabController = customerTabController;
    }

    /**
     * Registrace kontroleru záložky půjček hlavného pohledu
     *
     * @param borrowTabController kontroler záložky půjček
     */
    public void setBorrowTabController(BorrowTabController borrowTabController) {
        this.borrowTabController = borrowTabController;
    }

    /**
     * Registrace kontroleru záložky oznámení hlavného pohledu
     *
     * @param notificationTabController kontroler záložky oznámení
     */
    public void setNotificationTabController(NotificationTabController notificationTabController) {
        this.notificationTabController = notificationTabController;
    }

    /**
     * Registrace všech kontrolerů z hlavního pohledu
     *
     * @param bookTabController kontroler záložky knih
     * @param customerTabController kontroler záložky zákazníků
     * @param borrowTabController kontroler záložky půjček
     * @param notificationTabController kontroler záložky oznámení
     */
    public void setControllers(BookTabController bookTabController, CustomerTabController customerTabController, BorrowTabController borrowTabController, NotificationTabController notificationTabController) {
        this.bookTabController = bookTabController;
        this.customerTabController = customerTabController;
        this.borrowTabController = borrowTabController;
        this.notificationTabController = notificationTabController;
    }

    /**
     * Obnoví záložku knih
     */
    public void refreshBookTab() {
        if (bookTabController == null) {
            return;
        }
        System.out.println("Refresh book");
        bookTabController.updateView();
    }

    /**
     * Obnoví záložku zákazníků
     */
    public void refreshCustomerTab() {

        if (customerTabController == null) {
            return;
        }
        System.out.println("Refresh customer");
        customerTabController.updateView();
    }

    /**
     * Obnoví záložku půjček
     */
    public void refreshBorrowTab() {

        if (borrowTabController == null) {
            return;
        }
        System.out.println("Refresh borrow");
        borrowTabController.updateView();
    }

    /**
     * Obnoví záložku oznámení
     */
    public void refreshNotificationTab() {

        if (notificationTabController == null) {
            return;
        }
        System.out.println("Refresh notification");
        notificationTabController.updateView();
    }

    /**
     * Obnoví celý hlavní pohled
     */
    public void refreshAll() {
        refreshBookTab();
        refreshBorrowTab();
        refreshCustomerTab();
        refreshNotificationTab();
    }
}
