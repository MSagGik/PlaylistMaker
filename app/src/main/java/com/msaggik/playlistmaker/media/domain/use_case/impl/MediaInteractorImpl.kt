package com.msaggik.playlistmaker.media.domain.use_case.impl

import com.msaggik.playlistmaker.media.domain.use_case.MediaInteractor
import com.msaggik.playlistmaker.media.domain.repository.MediaRepository
import java.util.concurrent.Executors

class MediaInteractorImpl (
    private val repository: MediaRepository
) : MediaInteractor {

    val executor = Executors.newCachedThreadPool()
    override fun readTrackListMedia(consumer: MediaInteractor.MediaConsumer) {
        executor.execute {
            consumer.consume(repository.readTrackListHistory())
        }
    }
}