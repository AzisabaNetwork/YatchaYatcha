package com.github.aburaagearou.yatchayatcha;

import org.bukkit.Sound;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 設定ファイルユーティリティクラス
 * @author AburaAgeTarou
 */
public class YYConfigUtil {

	public static final String COUNTDOWN_KEY = "countdown";
	public static final String RULE_KEY = "rule";
	public static final String BID_MIN_PRICE_MULTI = "bid-min-price-multi";
	public static final String BID_MIN_PRICE_AD = "bid-min-price-add";
	public static final String LUNACHAT_AUCTION_CHANNEL = "lc-auction-channel";

	// 効果音設定
	public static final String SOUND_EFFECTS = "sound-effects";
	public static final String SE_START = "start";
	public static final String SE_BID_SELF = "bid-self";
	public static final String SE_BID_RIVAL = "bid-rival";
	public static final String SE_COUNTDOWN = "countdown";
	public static final String SE_SUCCESS = "success";

	// 効果音リスト
	private static final Map<String, Sound> sounds = new HashMap<>();
	static {
		Arrays.stream(Sound.values()).forEach(sound -> sounds.put(sound.name().replace(".", "_"), sound));
	}

	// 効果音設定
	private static final Map<String, List<SoundSetting>> soundSettings = new HashMap<>();

	/**
	 * 再読み込み
	 */
	public static void reload() {
		YatchaYatcha.getInstance().saveDefaultConfig();
		YatchaYatcha.getInstance().reloadConfig();
		soundSettings.clear();
	}

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

	/**
	 * LunaChatのオークションチャンネル名を取得
	 * @return チャンネル名(String)
	 */
	public static String getLunaChatAuctionChannel() {
		return YatchaYatcha.getInstance().getConfig().getString(LUNACHAT_AUCTION_CHANNEL);
	}

	/**
	 * 効果音設定を取得
	 * @param seKey sound-effects配下のキー名
	 * @return 効果音設定リスト
	 */
	@Nullable
	public static List<SoundSetting> getSoundSetting(String seKey) {
		String key = SOUND_EFFECTS + "." + seKey;
		if(!soundSettings.containsKey(seKey)) {
			if(!YatchaYatcha.getInstance().getConfig().isString(key)) {
				return null;
			}
			List<SoundSetting> settings = Arrays.stream(YatchaYatcha.getInstance().getConfig().getString(key).split(","))
					.map(YYConfigUtil::parseSoundSetting)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			soundSettings.put(seKey, settings);
		}
		return soundSettings.get(seKey);
	}

	/**
	 * 効果音設定の分解
	 * @param key 設定キー("SOUND_NAME-VOLUME-PITCH(-DELAY)")
	 */
	@Nullable
	public static SoundSetting parseSoundSetting(String key) {
		// 設定の分解("SOUND_NAME-VOLUME-PITCH(-DELAY)")
		String[] soundSetting = key.split("-");
		if(soundSetting.length < 3) {
			return null;
		}

		// 効果音の取得
		Sound sound = sounds.get(soundSetting[0].toUpperCase());
		if(sound == null) return null;
		BigDecimal volume = new BigDecimal(soundSetting[1]);
		BigDecimal pitch = new BigDecimal(soundSetting[2]);
		BigDecimal delay = new BigDecimal(0);
		if(soundSetting.length >= 4) {
			delay = new BigDecimal(soundSetting[3]);
		}
		return new SoundSetting(sound, volume.floatValue(), pitch.floatValue(), delay.intValue());
	}

	/**
	 * 効果音設定
	 */
	public static class SoundSetting {
		private final Sound sound;
		private final float volume;
		private final float pitch;
		private final int delay;

		/**
		 * コンストラクタ
		 * @param sound 効果音
		 * @param volume 音量
		 * @param pitch ピッチ
		 */
		public SoundSetting(Sound sound, float volume, float pitch, int delay) {
			this.sound = sound;
			this.volume = volume;
			this.pitch = pitch;
			this.delay = delay;
		}

		/**
		 * 効果音を取得
		 * @return 効果音
		 */
		public Sound getSound() {
			return sound;
		}

		/**
		 * 音量を取得
		 * @return 音量
		 */
		public float getVolume() {
			return volume;
		}

		/**
		 * ピッチを取得
		 * @return ピッチ
		 */
		public float getPitch() {
			return pitch;
		}

		/**
		 * 遅延時間を取得
		 * @return 遅延時間
		 */
		public int getDelay() {
			return delay;
		}
	}
}
