package com.app.study

import android.content.Context
import android.graphics.drawable.Drawable
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.app.android.databinding.FragmentMarkdownBinding
import com.app.mvvm.AbsVmVbFragment
import com.app.mvvm.base.EmptyViewModel
import com.app.navigator.popBackStack
import com.app.path.Page
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.Target
import io.noties.markwon.Markwon
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.glide.GlideImagesPlugin
import java.io.IOException

@Route(path = Page.MARKDOWN_FRAGMENT)
class MarkdownFragment : AbsVmVbFragment<EmptyViewModel, FragmentMarkdownBinding>() {
    @JvmField
    @Autowired(name = "title")
    var title: String? = ""

    @JvmField
    @Autowired(name = "info")
    var md: String? = ""

    private val markDown by lazy {

        Markwon.builder(requireContext())
            // .usePlugin(SyntaxHighlightPlugin.create(Prism4j(GrammarLocator())))
            // .usePlugin(GlideImagesPlugin.create(requireContext()))
            // .usePlugin(GlideImagesPlugin.create(Glide.with(this)))
            .usePlugin(
                GlideImagesPlugin.create(
                    object : GlideImagesPlugin.GlideStore {
                        override fun load(drawable: AsyncDrawable): RequestBuilder<Drawable> {
                            return Glide.with(requireActivity()).load(drawable.destination)
                        }

                        override fun cancel(target: Target<*>) {
                            Glide.with(requireActivity()).clear(target)
                        }
                    },
                ),
            ).build()
    }

    override fun initView() {
        ARouter.getInstance().inject(this)
        viewBind.back.setOnClickListener {
            popBackStack()
        }
        viewBind.title.text = title
    }

    override fun initData() {
        readFromAssets(md!!, requireContext())?.let { content ->
            markDown.setMarkdown(viewBind.markdownText, content)
        }
    }

    // 从assets中读取文件为String
    private fun readFromAssets(
        fileName: String,
        context: Context,
    ): String? {
        var result: String? = null
        result =
            try {
                val `is` = context.assets.open(fileName)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer, charset("UTF-8"))
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }
        return result
    }
}
