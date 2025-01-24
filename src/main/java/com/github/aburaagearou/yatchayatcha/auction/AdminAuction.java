package com.github.aburaagearou.yatchayatcha.auction;

import com.github.aburaagearou.yatchayatcha.YYConfigUtil;
import com.github.aburaagearou.yatchayatcha.YatchaYatcha;
import com.github.aburaagearou.yatchayatcha.log.AuctionInfo;
import com.github.aburaagearou.yatchayatcha.utils.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 運営オークション管理クラス
 * @author AburaAgeTarou
 */
public class AdminAuction {

	// 入札情報
	public static int    bidPrice  = 0;
	public static Player bidPlayer = null;

	public static BukkitTask countdownTask;

	// オークション中フラグ
	public static boolean isAuctioning = false;

	// サイドバー
	SidebarManager sidebar = new SidebarManager("&d運営オークション");

	// デバッグモード
	boolean isDebug = false;

	// 出品物情報
	ItemStack item;
	Component itemName;
	Component itemInfo;

	/**
	 * コンストラクタ
	 */
	private AdminAuction() {}

	/**
	 * インスタンス取得
	 */
	private static AdminAuction instance = null;
	public static AdminAuction getInstance() {
		if(instance == null) {
			instance = new AdminAuction();
		}
		return instance;
	}

	/**
	 * オークションを開始する
	 * @param item 出品物
	 */
	public void start(ItemStack item, int start, boolean isDebug) {
		if(isAuctioning) throw new IllegalStateException("他のオークションが進行中です");
		isAuctioning = true;

		// クリア
		bidPlayer = null;
		bidPrice = start;
		this.item = item;

		this.isDebug = isDebug;

		// 出品物名
		Component itemNameComp = item.getItemMeta().displayName();
		String itemName = itemNameComp != null ? LegacyComponentSerializer.legacyAmpersand().serialize(itemNameComp) : item.getType().name();
		this.itemName = LegacyComponentSerializer.legacyAmpersand().deserialize(itemName);

		// メッセージ
		itemInfo = this.itemName.append(Component.newline());
		List<Component> lore = item.getItemMeta().lore();
		if(lore != null) {
			lore.forEach(row -> itemInfo = itemInfo.append(row).append(Component.newline()));
		}
		TextComponent itemNameText =
				LegacyComponentSerializer.legacyAmpersand().deserialize("出品物： &r&f" + itemName)
				 	.hoverEvent(HoverEvent.showText(itemInfo));
		if(this.isDebug) Utilities.broadcastColoredMessage("&c!!!!!!!!!!!!!!!!Debug!!!!!!!!!!!!!!!!");
		else             Utilities.broadcastColoredMessage("&e=====================================");
		Utilities.broadcastColoredMessage("&r");
		Utilities.broadcastColoredMessage("&c&m-----&c&l&n 運営オークションが開始されました &c&m-----");
		Utilities.broadcastColoredMessage("&r");
		Bukkit.broadcast(itemNameText);
		Utilities.broadcastColoredMessage("&d&l開始価格： &e$" + bidPrice);
		Utilities.broadcastColoredMessage("&r");
		Utilities.broadcastColoredMessage("&e&l&n/bid (値段)&f コマンドで入札できます。");
		if(this.isDebug) {
			Utilities.broadcastColoredMessage("&r");
			Utilities.broadcastColoredMessage("&e※デバッグモードのため景品、費用の収受は行われません。");
		}
		Utilities.broadcastColoredMessage("&r");
		if(this.isDebug) Utilities.broadcastColoredMessage("&c!!!!!!!!!!!!!!!!Debug!!!!!!!!!!!!!!!!");
		else             Utilities.broadcastColoredMessage("&e=====================================");

		// サイドバー設定
		double multi = YYConfigUtil.getBidMinPriceMulti();
		int add = YYConfigUtil.getBidMinPriceAdd();
		BigDecimal minPrice = new BigDecimal(((double) bidPrice) * multi).add(new BigDecimal(add)).setScale(0, RoundingMode.UP);
		sidebar.addLine("&0");
		sidebar.addLine("&7出品物: &f" + itemName);
		sidebar.addLine("&1");
		sidebar.addLine("&7最高入札者: &aなし");
		sidebar.addLine("&7最高入札額: &e$" + bidPrice);
		sidebar.addLine("&e$" + (minPrice.intValue()+1) + "&fから入札可能");
		sidebar.addLine("&3");
		sidebar.addLine("&4");

		// 表示
		sidebar.showAll();
		sidebar.show();
	}

	/**
	 * カウントダウンタスクをキャンセルする
	 */
	private void cancelTask() {
		if(countdownTask != null) {
			Bukkit.getScheduler().cancelTask(countdownTask.getTaskId());
		}
	}

	/**
	 * オークションを終了する
	 */
	public void end(boolean cancel) {
		isAuctioning = false;
		if(isDebug) Utilities.broadcastColoredMessage("&c!!!!!!!!!!!!!!!!Debug!!!!!!!!!!!!!!!!");
		else        Utilities.broadcastColoredMessage("&e=====================================");
		Utilities.broadcastColoredMessage("&r");

		if(!cancel) {
			Utilities.broadcastColoredMessage("&c&m-----&c&l&n 運営オークションが終了しました &c&m-----");
		}
		else {
			Utilities.broadcastColoredMessage("&c&m-----&c&l&n 運営オークションが中止されました &c&m-----");
			bidPlayer = null;
		}

		Utilities.broadcastColoredMessage("&r");
		TextComponent itemNameText =
				LegacyComponentSerializer.legacyAmpersand().deserialize("出品物  ： &r&f").append(itemName)
						.hoverEvent(HoverEvent.showText(itemInfo));
		Bukkit.broadcast(itemNameText.asComponent());
		if(bidPlayer != null) {
			Utilities.broadcastColoredMessage("&d&l落札者  ： &e" + bidPlayer.getName());
			Utilities.broadcastColoredMessage("&d&l落札価格： &e$" + bidPrice);
			if(YatchaYatcha.getEconomy().getBalance(bidPlayer) < bidPrice) {
				Utilities.broadcastColoredMessage("&r");
				Utilities.broadcastColoredMessage("&4~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				Utilities.broadcastColoredMessage("&c&l落札者の所持金が足りないためキャンセルされました。");
				Utilities.broadcastColoredMessage("&4~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			}
			else {
				if(!isDebug) {
					YatchaYatcha.getEconomy().withdrawPlayer(bidPlayer, bidPrice);
					bidPlayer.getInventory().addItem(item);
				}
				else {
					Utilities.broadcastColoredMessage("&r");
					Utilities.broadcastColoredMessage("&e※デバッグモードのため景品、費用の収受は行われません。");
				}
			}
		}
		else {
			Utilities.broadcastColoredMessage("&d&l落札者はいませんでした。");
		}
		Utilities.broadcastColoredMessage("&r");
		if(isDebug) Utilities.broadcastColoredMessage("&c!!!!!!!!!!!!!!!!Debug!!!!!!!!!!!!!!!!");
		else        Utilities.broadcastColoredMessage("&e=====================================");

		// ロギング
		YatchaYatcha.log(new AuctionInfo(bidPlayer, bidPrice, item));

		// スコアボード使用終了
		sidebar.clear();
		sidebar.hide();
	}

	/**
	 * カウントダウンによる自動終了
	 */
	public void countdown() {
		int countdown = YYConfigUtil.getCountdown();

		// カウントダウン
		final int price = bidPrice;
		countdownTask = Bukkit.getScheduler().runTaskTimer(YatchaYatcha.getInstance(), new Runnable() {
			int count = countdown;

			@Override
			public void run() {
				if(!isAuctioning) {
					cancelTask();
					return;
				}
				if(count-- <= 0) {
					end(false);
					cancelTask();
					return;
				}

				// 入札があった場合
				if(price != bidPrice) {
					cancelTask();
					return;
				}

				// 10秒前
				if(count == 10) {
					Utilities.broadcastColoredMessage("&a&l入札がない場合、あと&e&n" + count + "&a&l秒でオークションが終了します！");
				}
				else if(count < 5) {
					Utilities.broadcastColoredMessage("&e&n" + (count+1) + "...");
				}

				// サイドバー更新
				sidebar.setLine(7, "&b落札まであと&e" + (count+1) + "秒");
			}
		}, 20L, 20L);
	}

	/**
	 * 入札
	 * @param player 入札者
	 * @param price  入札額
	 */
	public void bid(Player player, int price) {
		if(!isAuctioning) {
			Utilities.sendColoredMessage(player, "&c現在オークションは行われていません。");
			return;
		}

		// 入札額チェック
		double multi = YYConfigUtil.getBidMinPriceMulti();
		int add = YYConfigUtil.getBidMinPriceAdd();
		BigDecimal minPrice = new BigDecimal(((double) bidPrice) * multi).add(new BigDecimal(add)).setScale(0, RoundingMode.UP);
		if(price < minPrice.intValue()) {
			Utilities.sendColoredMessage(player, "&c入札額は &e&l$" + minPrice.intValue() + " &cより高くしてください。");
			return;
		}
		if(YatchaYatcha.getEconomy().getBalance(player) < price) {
			Utilities.sendColoredMessage(player, "&c所持金が足りません。");
			return;
		}
		Utilities.broadcastColoredMessage("&#FFFF00［✩入札情報✩］ &d&n" + player.getName() + "&r &7が &e&l$" + price + "&r &7で入札！");
		bidPlayer = player;
		bidPrice = price;

		// サイドバー更新
		minPrice = new BigDecimal(((double) bidPrice) * multi).add(new BigDecimal(add)).setScale(0, RoundingMode.UP);
		sidebar.setLine(3, "&7最高入札者: &a" + bidPlayer.getName());
		sidebar.setLine(4, "&7最高入札額: &e$" + bidPrice);
		sidebar.setLine(5, "&e$" + (minPrice.intValue()+1) + "&fから入札可能");

		// カウントダウン開始
		cancelTask();
		countdown();
	}
}
