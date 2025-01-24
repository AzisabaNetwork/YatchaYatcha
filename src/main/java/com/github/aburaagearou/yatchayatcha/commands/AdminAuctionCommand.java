package com.github.aburaagearou.yatchayatcha.commands;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.aburaagearou.yatchayatcha.YatchaYatcha;
import com.github.aburaagearou.yatchayatcha.auction.AdminAuction;
import com.github.aburaagearou.yatchayatcha.utils.Utilities;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 運営オークション管理コマンド クラス
 * @author AburaAgeTarou
 */
@CommandAlias("adminauction")
@CommandPermission("yatchayatcha.adminauction")
public class AdminAuctionCommand extends YYBaseCommand {

	@Dependency
	private YatchaYatcha plugin;

	@Default
	@HelpCommand
	public void onHelp(CommandSender sender, CommandHelp help) {
		help.showHelp();
	}

	@Subcommand("start")
	@Description("運営オークションを開始します。")
	public void onAdminAuction(Player player, @Default("100") int start, @Default("") String debug) {
		// 所持アイテムを競売に出品
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.getType() == Material.AIR) {
			Utilities.sendColoredMessage(player, "&a[YatchaYatcha] &c出品物を手に持ってください。");
			return;
		}

		// オークション開始
		try {
			AdminAuction auction = AdminAuction.getInstance();
			auction.start(item, start, !debug.isEmpty());
		}
		catch (Exception e) {
			Utilities.sendColoredMessage(player, "&c" + e.getMessage());
		}
	}

	@Subcommand("end")
	@Description("運営オークションを終了します。")
	public void onAdminAuctionEnd(Player player, @Default("5") int countdown) {
		AdminAuction auction = AdminAuction.getInstance();
		auction.end(countdown);
	}

	@Subcommand("cancel")
	@Description("運営オークションを中止します。")
	public void onAdminAuctionCancel(Player player) {

	}
}
