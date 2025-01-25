package com.github.aburaagearou.yatchayatcha.auction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * サードバー管理クラス
 * @author AburaAgeTarou
 */
public class SidebarManager {

	// オブジェクティブID
	private static final String OBJECTIVE_NAME = "yy_sidebar_obj";

	// スコアボード
	private final Scoreboard scoreboard;

	// 表示対象プレイヤーリスト
	// nullの場合は全プレイヤー
	private List<Player> player = null;

	// サイドバー表示文字列
	private final List<String> lines = new ArrayList<>();
	private final List<String> beforeLines = new ArrayList<>();

	/**
	 * コンストラクタ
	 */
	public SidebarManager(Component displayName) {
		// スコアボードを作成
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		// オブジェクティブを登録
		this.scoreboard.registerNewObjective(OBJECTIVE_NAME, "dummy", displayName, RenderType.INTEGER);


	}
	public SidebarManager(String objectiveName) {
		this(LegacyComponentSerializer.legacyAmpersand().deserialize(objectiveName));
	}

	/**
	 * サイドバーに表示
	 */
	public void show() {
		Objective objective = this.scoreboard.getObjective(OBJECTIVE_NAME);
		if (objective == null) return;
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	/**
	 * サイドバーから非表示
	 */
	public void hide() {
		Objective objective = this.scoreboard.getObjective(OBJECTIVE_NAME);
		if (objective == null) return;
		objective.setDisplaySlot(null);
	}

	/**
	 * 内容をクリア
	 */
	public void clear() {
		this.lines.clear();
		update();
	}

	/**
	 * 表示対象プレイヤーを追加
	 * @param players 表示対象プレイヤー
	 */
	public void addPlayer(Player ...players) {
		if (this.player == null) {
			this.player = new ArrayList<>(Arrays.asList(players));
		}
		else {
			this.player.addAll(Arrays.asList(players));
		}
	}

	/**
	 * 表示対象プレイヤーを削除
	 * @param players 表示対象プレイヤー
	 */
	public void removePlayer(Player ...players) {
		if (this.player != null) {
			this.player.removeAll(Arrays.asList(players));
		}
	}

	/**
	 * 表示対象プレイヤーをクリア
	 */
	public void clearPlayer() {
		this.player.clear();
	}

	/**
	 * 全員に表示
	 */
	public void showAll() {
		this.player = null;
		this.update();
	}

	/**
	 * 表示更新
	 */
	public void update() {
		// スコア更新
		updateScores();

		// 全員に表示
		if (this.player == null) {
			Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(this.scoreboard));
		}
		// 指定プレイヤーに表示
		else {
			this.player.forEach(player -> player.setScoreboard(this.scoreboard));
		}
	}

	/**
	 * スコア更新
	 */
	public void updateScores() {
		// リセット
		this.beforeLines.forEach(this.scoreboard::resetScores);

		// 表示
		Objective objective = this.scoreboard.getObjective(OBJECTIVE_NAME);
		if(objective == null) return;
		for (int i = (this.lines.size() - 1), j = 0; i >= 0; i--, j++) {
			objective.getScore(this.lines.get(j)).setScore(i);
		}

		// 設定中の行を保存
		beforeLines.clear();
		beforeLines.addAll(lines);
	}

	/**
	 * 文字列を指定行に挿入
	 * 指定行が存在しない場合は末尾に追加
	 * @param line 行番号(0から開始、-1で末尾)
	 * @param text 挿入する文字列
	 */
	public void insertLine(int line, String text) {
		// 重複チェック
		if(this.lines.stream().anyMatch(text::equals)) return;

		// 追加
		if(line < 0 || line >= this.lines.size()) this.lines.add(ChatColor.translateAlternateColorCodes('&', text));
		else this.lines.add(line, ChatColor.translateAlternateColorCodes('&', text));

		update();
	}

	/**
	 * 文字列を末尾に追加
	 * @param text 追加する文字列
	 */
	public void addLine(String text) {
		insertLine(-1, text);
	}

	/**
	 * 文字列を末尾に追加
	 * @param text 追加する文字列
	 */
	public void setLine(int line, String text) {
		if(line < 0 || line >= this.lines.size()) return;
		this.lines.set(line, ChatColor.translateAlternateColorCodes('&', text));
		update();
	}
}
