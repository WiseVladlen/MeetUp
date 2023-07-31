package com.example.meet_up.di

import dagger.Module

@Module(includes = [LocalModule::class, BindingModule::class])
class MainModule