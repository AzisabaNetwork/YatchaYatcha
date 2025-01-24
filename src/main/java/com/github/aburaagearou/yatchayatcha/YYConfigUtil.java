package com.github.aburaagearou.yatchayatcha;

import java.util.List;

/**
 * 設定ファイルユーティリティクラス
 * @author AburaAgeTarou
 */
public class YYConfigUtil {

	public static final String COUNTDOWN_KEY = "countdown";
	public static final String RULE_KEY = "rule";
	public static final String BID_MIN_PRICE_MULTI = "bid-min-price-multi";
	public static final String BID_MIN_PRICE_AD = "bid-min-price-add";

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

	/**
	 * 入札最低価格の倍率を取得
	 * @return 倍率(double)
	 */
	public static double getBidMinPriceMulti() {
		return YatchaYatcha.getInstance().getConfig().getDouble(BID_MIN_PRICE_MULTI);
	}

	/**
	 * 入札最低価格の加算値を取得
	 * @return 加算値(int)
	 */
	public static int getBidMinPriceAdd() {
		return YatchaYatcha.getInstance().getConfig().getInt(BID_MIN_PRICE_AD);
	}
}
