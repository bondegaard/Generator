package dk.bondegaard.generator.utils;

import dk.bondegaard.generator.Main;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;


public abstract class GUI implements Listener {

    protected final Inventory inv;

    protected final Player player;

    protected Map<Integer, ItemStack> layout = new HashMap<Integer, ItemStack>();

    protected Map<Integer, SlotType> layoutLock = new HashMap<>();

    protected ArrayList<ItemStack> page_items = new ArrayList<ItemStack>();

    //pages
    int PAGE = 1;
    int MAX_PAGES = 1;

    private int rows = 6;

    private long clickCooldown = 250;

    private final long lastClick = System.currentTimeMillis();

    public enum GUIItem {

        CLOSE(new ItemBuilder(Material.BARRIER, 1, (byte) 0, "§c§lCLOSE MENU", "", "§7Press to close the menu.").build()),
        GLASS_FILL(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 0, "§r").build()),
        NEXT(new ItemBuilder(Material.ARROW, 1, (byte) 0, "§rNEXT PAGE").build()),
        PREVIOUS(new ItemBuilder(Material.ARROW, 1, (byte) 0, "§rPREVIOUS PAGE").build());

        private final ItemStack item;

        GUIItem(ItemStack item) {
            this.item = item;
        }

        public ItemStack getItem() {
            return this.item;
        }
    }

    public boolean pageBack() {
        return 1 < PAGE;
    }

    public boolean pageForward() {
        return MAX_PAGES > PAGE;
    }

    public void backPage() {
        if (!this.pageBack()) return;
        this.PAGE--;
        this.open(player);
    }

    public void nextPage() {
        if (!this.pageForward()) return;
        this.PAGE++;
        this.open(player);
    }

    public int maxPageDisplaySize() {
        if (this.PAGE * 45 > this.page_items.size()) {
            return this.page_items.size();
        } else {
            return this.PAGE * 45;
        }
    }

    public void updatePageAmount() {
        this.MAX_PAGES = (int) Math.ceil(page_items.size() / 45.0);
    }

    public boolean usePages() {
        return this.page_items.size() > 0;
    }

    public GUI(String name, int rows, Player player) {
        this.player = player;
        this.rows = rows;
        inv = Main.getInstance().getServer().createInventory(null, rows * 9, StringUtil.colorize(name));
    }

    private List<Pair<Integer, ItemStack>> permPageItem = new ArrayList<>();

    public void render() {


        if (usePages()) {
            updatePageAmount();
        }

        inv.clear();


        if (this.pageBack() && usePages()) {
            inv.setItem(47, GUIItem.PREVIOUS.getItem());
        }

        if (usePages()) {
            inv.setItem(49, GUIItem.CLOSE.getItem());
        }


        if (this.pageForward() && usePages()) {
            inv.setItem(51, GUIItem.NEXT.getItem());
        }

        if (!this.permPageItem.isEmpty() && usePages()) {
            for (Pair<Integer, ItemStack> i : permPageItem) {
                inv.setItem(i.getLeft(), i.getRight());
            }
        }


        for (Map.Entry<Integer, ItemStack> set : layout.entrySet()) {
            inv.setItem(set.getKey(), set.getValue());
        }


        if (usePages()) {
            int fromIndex = (PAGE - 1) * 45;
            int toIndex = maxPageDisplaySize();

            for (ItemStack is : this.page_items.subList(fromIndex, toIndex)) {
                inv.addItem(is);
            }
        }


    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, int amount, final int metaval, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, amount, (short) metaval);
        final ItemMeta meta = item.getItemMeta();
        // Set the name of the item
        meta.setDisplayName(name);
        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public void unlockInventorySlots() {
        for (int i = rows * 9 + 1; i < rows * 9 * 4 + 1; i++)
            this.layoutLock.put(i, SlotType.OPEN);
    }

    // You can open the inventory with this
    public void open(final HumanEntity ent) {
        this.render();
        ent.openInventory(inv);
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);
        if (System.currentTimeMillis() - lastClick < clickCooldown) {
            return;
        }
        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        //final Player p = (Player) e.getWhoClicked();

        int slot = e.getRawSlot();

        SlotType slotType = layoutLock.getOrDefault(slot, SlotType.DEFAULT);

        if (slotType != SlotType.LOCKED) this.click(slot, clickedItem, e.isShiftClick());

        if (slotType != SlotType.OPEN) e.setCancelled(true);
    }

    public void click(int slot, ItemStack clickedItem, boolean shift) {
    }


    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {

        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().equals(inv)) {
            HandlerList.unregisterAll(this);
        }
    }

    public enum SlotType {
        LOCKED,  // Clicks will not register
        OPEN,    // Items can be removed, clicks still register
        DEFAULT //Items can't be removed but clicks are registered
    }

    public void setClickCooldown(long clickCooldown) {this.clickCooldown = clickCooldown;}

    public void setPermPageItem(List<Pair<Integer, ItemStack>> permPageItem) {this.permPageItem = permPageItem;}
}
