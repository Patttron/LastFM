package teach.meskills.lastfm.chartTracks

import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.annotation.GlideModule

@GlideModule
class MyAppGlideModule: AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean {
        return super.isManifestParsingEnabled()
    }
}