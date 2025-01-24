package com.github.aburaagearou.yatchayatcha.utils;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
	}
}
