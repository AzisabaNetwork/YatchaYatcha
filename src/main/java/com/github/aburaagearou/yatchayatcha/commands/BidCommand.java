package com.github.aburaagearou.yatchayatcha.commands;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.github.aburaagearou.yatchayatcha.YatchaYatcha;
import com.github.aburaagearou.yatchayatcha.auction.AdminAuction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 入札コマンド クラス
 * @author AburaAgeTarou
 */
public class BidCommand extends YYBaseCommand {

	@Dependency
	private YatchaYatcha plugin;

	@HelpCommand
	public void onHelp(CommandSender sender, CommandHelp help) {
		help.showHelp();
	}

	@CommandAlias("bid")
	@Description("指定した値段で入札します")
	public void onBid(Player player, int price) {
		AdminAuction.getInstance().bid(player, price);
	}
}
