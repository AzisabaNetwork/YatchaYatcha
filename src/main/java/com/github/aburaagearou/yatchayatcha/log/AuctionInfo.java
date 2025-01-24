package com.github.aburaagearou.yatchayatcha.log;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

/**
 * ロギング用オークション情報
 */
public class AuctionInfo {

	// 落札者
	public OfflinePlayer bidder;
	public int price;
	public ItemStack item;

	/**
	 * コンストラクタ
	 * @param bidder 落札者
	 * @param price 価格
	 * @param item アイテム
	 */
	public AuctionInfo(OfflinePlayer bidder, int price, ItemStack item) {
		this.bidder = bidder;
		this.price = price;
		this.item = item;
	}

	/**
	 * ログ文字列を取得
	 * @return ログ文字列
	 */
	public String getLogString() {
		Component itemNameComp = item.getItemMeta().displayName();
		String itemName = item.getType().name();
		if(itemNameComp != null) {
			itemName = PlainComponentSerializer.plain().serialize(itemNameComp);
		}
		return "落札者: " + bidder.getName() + " 価格: " + price + " 品物: " + itemName;
	}

	/**
	 * ログ文字列を取得
	 * @return ログ文字列
	 */
	public String getColoredLogString() {
		Component itemNameComp = item.getItemMeta().displayName();
		String itemName = item.getType().name();
		if(itemNameComp != null) {
			itemName = PlainComponentSerializer.plain().serialize(itemNameComp);
		}
		return "&d落札者: &a" + bidder.getName() + " &d価格: &a$" + price + " &d品物: &r" + itemName;
	}
}
