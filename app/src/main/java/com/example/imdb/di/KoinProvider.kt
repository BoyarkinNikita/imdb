package com.example.imdb.di

import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

inline fun <reified T> get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = GlobalContext.get().koin.get(qualifier, parameters)

inline fun <reified T> inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = GlobalContext.get().koin.inject(qualifier, parameters)

inline fun <reified S, reified P> bind(
    noinline parameters: ParametersDefinition? = null
): S = GlobalContext.get().koin.bind<S, P>(parameters)