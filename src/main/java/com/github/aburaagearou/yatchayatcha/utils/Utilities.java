package com.github.aburaagearou.yatchayatcha.utils;

import com.github.aburaagearou.yatchayatcha.YYConfigUtil;
import com.github.aburaagearou.yatchayatcha.YatchaYatcha;
import com.github.aburaagearou.yatchayatcha.hook.LunaChatHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

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

	/**
	 * 効果音を再生
	 * @param sounds 効果音
	 * @param players 対象プレイヤー(Nullの場合は全員)
	 */
	public static void playSound(Collection<YYConfigUtil.SoundSetting> sounds, @Nullable Player ...players) {
		if(sounds == null || sounds.isEmpty()) return;

		// 対象プレイヤー
		List<Player> playerList;
		if(players != null && players.length > 0) {
			playerList = new ArrayList<>(Arrays.asList(players));
		}
		else {
			playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
		}

		// 遅延時間の昇順でソート
		List<YYConfigUtil.SoundSetting> sorted = new ArrayList<>(sounds);
		sorted.sort(Comparator.comparingInt(YYConfigUtil.SoundSetting::getDelay));

		// 遅延時間を考慮して再生
		int maxDelay = sorted.stream().max(Comparator.comparingInt(YYConfigUtil.SoundSetting::getDelay)).get().getDelay();
		SyncEnhancedTask syncedTask = new SyncEnhancedTask();
		syncedTask.loop(maxDelay, 1L, (task) -> {
			int count = (syncedTask.getCount() - 1);
			for(YYConfigUtil.SoundSetting sound : sorted) {
				if(count == sound.getDelay()) {
					playerList.forEach(player -> player.playSound(player.getLocation(), sound.getSound(), SoundCategory.MASTER, sound.getVolume(), sound.getPitch()));
				}
			}
		}).start();
	}
}
