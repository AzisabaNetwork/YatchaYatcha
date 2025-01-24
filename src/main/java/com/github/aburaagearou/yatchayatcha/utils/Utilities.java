package com.github.aburaagearou.yatchayatcha.utils;

import com.github.aburaagearou.yatchayatcha.YatchaYatcha;
import com.github.aburaagearou.yatchayatcha.hook.LunaChatHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * ユーティリティクラス
 * @author AburaAgeTarou
 */
public class Utilities {

	/**
	 * メッセージを&着色して送信
	 * @param sender 対象
	 * @param message メッセージ
	 */
	public static void sendColoredMessage(CommandSender sender, String message) {
		sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
	}

	/**
	 * メッセージを&着色してブロードキャスト
	 * @param message メッセージ
	 */
	public static void broadcastColoredMessage(String message) {
		broadcastColoredMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
	}
	public static void broadcastColoredMessage(Component message) {
		if(YatchaYatcha.isLunaChatEnabled()) {
			if(LunaChatHook.sendToLunaChatChannel(LegacyComponentSerializer.legacyAmpersand().serialize(message))) {
				return;
			}
		}
		Bukkit.broadcast(message);
	}
}
