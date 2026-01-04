package com.example.mycomposeapp.data.api

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

object SupabaseClient {
    private const val SUPABASE_URL = "https://cncbvnjmjmuwrdydpspt.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNuY2J2bmptam11d3JkeWRwc3B0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMyNjk3MzIsImV4cCI6MjA3ODg0NTczMn0.fmiP0oZfVv9LvTOHFVPvLLJ8_gPat-nXczyBU8Etc5w"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Storage)
    }

    val imageBucket = client.storage.from("post-photos")
}