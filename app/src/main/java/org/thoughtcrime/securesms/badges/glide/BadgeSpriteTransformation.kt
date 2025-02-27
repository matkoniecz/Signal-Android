package org.thoughtcrime.securesms.badges.glide

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Cuts out the badge of the requested size from the sprite sheet.
 */
class BadgeSpriteTransformation(
  private val size: Size,
  private val density: String,
  private val isDarkTheme: Boolean
) : BitmapTransformation() {

  private val id = "BadgeSpriteTransformation(${size.code},$density,$isDarkTheme).$VERSION"

  override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    messageDigest.update(id.toByteArray(CHARSET))
  }

  override fun equals(other: Any?): Boolean {
    return (other as? BadgeSpriteTransformation)?.id == id
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
    val outBitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(outBitmap)
    val inBounds = getInBounds(density, size, isDarkTheme)
    val outBounds = Rect(0, 0, outWidth, outHeight)

    canvas.drawBitmap(toTransform, inBounds, outBounds, null)

    return outBitmap
  }

  enum class Size(val code: String, val frameMap: Map<Density, FrameSet>) {
    SMALL(
      "small",
      mapOf(
        Density.LDPI to FrameSet(Frame(124, 1, 13, 13), Frame(145, 31, 13, 13)),
        Density.MDPI to FrameSet(Frame(163, 1, 16, 16), Frame(189, 39, 16, 16)),
        Density.HDPI to FrameSet(Frame(244, 1, 25, 25), Frame(283, 58, 25, 25)),
        Density.XHDPI to FrameSet(Frame(323, 1, 32, 32), Frame(373, 75, 32, 32)),
        Density.XXHDPI to FrameSet(Frame(483, 1, 48, 48), Frame(557, 111, 48, 48)),
        Density.XXXHDPI to FrameSet(Frame(643, 1, 64, 64), Frame(741, 147, 64, 64))
      )
    ),
    MEDIUM(
      "medium",
      mapOf(
        Density.LDPI to FrameSet(Frame(124, 16, 19, 19), Frame(160, 31, 19, 19)),
        Density.MDPI to FrameSet(Frame(163, 19, 24, 24), Frame(207, 39, 24, 24)),
        Density.HDPI to FrameSet(Frame(244, 28, 37, 37), Frame(310, 58, 37, 37)),
        Density.XHDPI to FrameSet(Frame(323, 35, 48, 48), Frame(407, 75, 48, 48)),
        Density.XXHDPI to FrameSet(Frame(483, 51, 72, 72), Frame(607, 111, 72, 72)),
        Density.XXXHDPI to FrameSet(Frame(643, 67, 96, 96), Frame(807, 147, 96, 96))
      )
    ),
    LARGE(
      "large",
      mapOf(
        Density.LDPI to FrameSet(Frame(145, 1, 28, 28), Frame(124, 46, 28, 28)),
        Density.MDPI to FrameSet(Frame(189, 1, 36, 36), Frame(163, 57, 36, 36)),
        Density.HDPI to FrameSet(Frame(283, 1, 55, 55), Frame(244, 85, 55, 55)),
        Density.XHDPI to FrameSet(Frame(373, 1, 72, 72), Frame(323, 109, 72, 72)),
        Density.XXHDPI to FrameSet(Frame(557, 1, 108, 108), Frame(483, 161, 108, 108)),
        Density.XXXHDPI to FrameSet(Frame(741, 1, 144, 144), Frame(643, 213, 144, 144))
      )
    ),
    BADGE_60(
      "badge_60",
      mapOf(
        Density.LDPI to FrameSet(Frame(124, 76, 45, 45), Frame(124, 76, 45, 45)),
        Density.MDPI to FrameSet(Frame(163, 101, 60, 60), Frame(163, 101, 60, 60)),
        Density.HDPI to FrameSet(Frame(244, 151, 90, 90), Frame(244, 151, 90, 90)),
        Density.XHDPI to FrameSet(Frame(323, 201, 120, 120), Frame(323, 201, 120, 120)),
        Density.XXHDPI to FrameSet(Frame(483, 301, 180, 180), Frame(483, 301, 180, 180)),
        Density.XXXHDPI to FrameSet(Frame(643, 401, 240, 240), Frame(643, 401, 240, 240))
      )
    ),
    XLARGE(
      "xlarge",
      mapOf(
        Density.LDPI to FrameSet(Frame(1, 1, 121, 121), Frame(1, 1, 121, 121)),
        Density.MDPI to FrameSet(Frame(1, 1, 160, 160), Frame(1, 1, 160, 160)),
        Density.HDPI to FrameSet(Frame(1, 1, 241, 241), Frame(1, 1, 241, 241)),
        Density.XHDPI to FrameSet(Frame(1, 1, 320, 320), Frame(1, 1, 320, 320)),
        Density.XXHDPI to FrameSet(Frame(1, 1, 480, 480), Frame(1, 1, 480, 480)),
        Density.XXXHDPI to FrameSet(Frame(1, 1, 640, 640), Frame(1, 1, 640, 640))
      )
    );

    companion object {
      fun fromInteger(integer: Int): Size {
        return when (integer) {
          0 -> SMALL
          1 -> MEDIUM
          2 -> LARGE
          3 -> XLARGE
          4 -> BADGE_60
          else -> LARGE
        }
      }
    }
  }

  enum class Density(val density: String) {
    LDPI("ldpi"),
    MDPI("mdpi"),
    HDPI("hdpi"),
    XHDPI("xhdpi"),
    XXHDPI("xxhdpi"),
    XXXHDPI("xxxhdpi")
  }

  data class FrameSet(val light: Frame, val dark: Frame)

  data class Frame(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
  ) {
    fun toBounds(): Rect {
      return Rect(x, y, x + width, y + height)
    }
  }

  companion object {
    private const val VERSION = 2

    private fun getDensity(density: String): Density {
      return Density.values().first { it.density == density }
    }

    private fun getFrame(size: Size, density: Density, isDarkTheme: Boolean): Frame {
      val frameSet: FrameSet = size.frameMap[density]!!
      return if (isDarkTheme) frameSet.dark else frameSet.light
    }

    private fun getInBounds(density: String, size: Size, isDarkTheme: Boolean): Rect {
      return getFrame(size, getDensity(density), isDarkTheme).toBounds()
    }
  }
}
