package com.github.aburaagearou.yatchayatcha;

import java.util.List;

/**
 * 設定ファイルユーティリティクラス
 * @author AburaAgeTarou
 */
public class YYConfigUtil {

	public static final String COUNTDOWN_KEY = "countdown";
	public static final String RULE_KEY = "rule";

	/**
	 * カウントダウン秒数を取得
	 * @return 秒数(int)
	 */
	public static int getCountdown() {
		return YatchaYatcha.getInstance().getConfig().getInt(COUNTDOWN_KEY);
	}

	/**
	 * オークションルールを取得
	 * @return ルールの文字列リスト
	 */
	public static List<String> getRule() {
		return YatchaYatcha.getInstance().getConfig().getStringList(RULE_KEY);
	}
}
