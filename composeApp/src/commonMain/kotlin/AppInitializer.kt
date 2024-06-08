import com.mrl.pixiv.common.di.LibDependencyInitializer
import com.mrl.pixiv.di.allModule
import org.koin.dsl.KoinAppDeclaration


object AppInitializer {
    fun onApplicationStart(platformDeclaration: KoinAppDeclaration = {}) {
        LibDependencyInitializer.initialize(platformDeclaration, allModule)
    }
}