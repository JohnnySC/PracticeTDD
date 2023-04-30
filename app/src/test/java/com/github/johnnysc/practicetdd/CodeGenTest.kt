package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 30.04.2023
 */
class CodeGenTest {

    //region source
    private val source = "package ru.easycode.words504.initial.presentation\n" +
            "\n" +
            "import junit.framework.Assert.assertEquals\n" +
            "import kotlinx.coroutines.runBlocking\n" +
            "import org.junit.Before\n" +
            "import org.junit.Test\n" +
            "import ru.easycode.words504.BaseTest\n" +
            "import ru.easycode.words504.MainScreen\n" +
            "import ru.easycode.words504.initial.domain.InitialInteractor\n" +
            "import ru.easycode.words504.initial.domain.InitialResult\n" +
            "import ru.easycode.words504.languages.presentation.ChooseLanguageScreen\n" +
            "import ru.easycode.words504.presentation.NavigationCommunication\n" +
            "import ru.easycode.words504.presentation.Screen\n" +
            "\n" +
            "class InitialViewModelTest : BaseTest() {\n" +
            "\n" +
            "    private lateinit var interactor: FakeInitialInteractor\n" +
            "    private lateinit var communication: FakeInitialCommunication\n" +
            "    private lateinit var navigation: FakeNavigation\n" +
            "    private lateinit var viewModel: InitialViewModel\n" +
            "\n" +
            "    @Before\n" +
            "    fun setUp() {\n" +
            "        super.init()\n" +
            "        interactor = FakeInitialInteractor.Base(functionsCallsStack)\n" +
            "        communication = FakeInitialCommunication.Base(functionsCallsStack)\n" +
            "        navigation = FakeNavigation.Base(functionsCallsStack)\n" +
            "        viewModel = InitialViewModel(\n" +
            "            interactor = interactor,\n" +
            "            communication = communication,\n" +
            "            navigation = navigation,\n" +
            "        )\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    fun `first opening success`() = runBlocking {\n" +
            "        interactor.changeExpected(InitialResult.FirstOpening)\n" +
            "        viewModel.init()\n" +
            "        communication.same(InitialUiState.Loading)\n" +
            "        interactor.same(InitialResult.FirstOpening)\n" +
            "        navigation.same(ChooseLanguageScreen)\n" +
            "        functionsCallsStack.checkStack(3)\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    fun `not first opening success`() = runBlocking {\n" +
            "        interactor.changeExpected(InitialResult.NotFirstOpening)\n" +
            "        viewModel.init()\n" +
            "        communication.same(InitialUiState.Loading)\n" +
            "        interactor.same(InitialResult.NotFirstOpening)\n" +
            "        navigation.same(MainScreen)\n" +
            "        functionsCallsStack.checkStack(3)\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    fun `first opening failure then retry and success`() = runBlocking {\n" +
            "        interactor.changeExpected(InitialResult.Error(message = \"no connection\"))\n" +
            "        viewModel.init()\n" +
            "        communication.same(InitialUiState.Loading)\n" +
            "        interactor.same(InitialResult.Error(message = \"no connection\"))\n" +
            "        communication.same(InitialUiState.Error(message = \"no connection\"))\n" +
            "\n" +
            "        interactor.changeExpected(InitialResult.FirstOpening)\n" +
            "        viewModel.retry()\n" +
            "        communication.same(InitialUiState.Loading)\n" +
            "        interactor.same(InitialResult.FirstOpening)\n" +
            "        navigation.same(ChooseLanguageScreen)\n" +
            "        functionsCallsStack.checkStack(6)\n" +
            "    }\n" +
            "\n" +
            "    private interface FakeInitialInteractor : InitialInteractor {\n" +
            "\n" +
            "        fun same(other: InitialResult)\n" +
            "\n" +
            "        fun changeExpected(expected: InitialResult)\n" +
            "\n" +
            "        class Base(private val functionCallsStack: FunctionsCallsStack) :\n" +
            "            FakeInitialInteractor {\n" +
            "            private lateinit var result: InitialResult\n" +
            "\n" +
            "            override fun same(other: InitialResult) {\n" +
            "                assertEquals(result, other)\n" +
            "                functionCallsStack.checkCalled(INTERACTOR_CALLED)\n" +
            "            }\n" +
            "\n" +
            "            override fun changeExpected(expected: InitialResult) {\n" +
            "                result = expected\n" +
            "            }\n" +
            "\n" +
            "            override suspend fun init(): InitialResult {\n" +
            "                functionCallsStack.put(INTERACTOR_CALLED)\n" +
            "                return result\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        companion object {\n" +
            "            private const val INTERACTOR_CALLED = \"interactor#init\"\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private interface FakeInitialCommunication : InitialCommunication {\n" +
            "\n" +
            "        fun same(other: InitialUiState)\n" +
            "\n" +
            "        class Base(private val functionCallsStack: FunctionsCallsStack) : FakeInitialCommunication {\n" +
            "            private val list = mutableListOf<InitialUiState>()\n" +
            "            private var index = 0\n" +
            "\n" +
            "            override fun map(source: InitialUiState) {\n" +
            "                functionCallsStack.put(COMMUNICATION_CALLED)\n" +
            "                list.add(source)\n" +
            "            }\n" +
            "\n" +
            "            override fun same(other: InitialUiState) {\n" +
            "                assertEquals(list[index++], other)\n" +
            "                functionCallsStack.checkCalled(COMMUNICATION_CALLED)\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        companion object {\n" +
            "            private const val COMMUNICATION_CALLED = \"communication#map\"\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private interface FakeNavigation : NavigationCommunication {\n" +
            "\n" +
            "        fun same(other: Screen)\n" +
            "\n" +
            "        class Base(private val functionCallsStack: FunctionsCallsStack) : FakeNavigation {\n" +
            "            private lateinit var screen: Screen\n" +
            "\n" +
            "            override fun same(other: Screen) {\n" +
            "                assertEquals(screen, other)\n" +
            "                functionCallsStack.checkCalled(NAVIGATION_CALLED)\n" +
            "            }\n" +
            "\n" +
            "            override fun map(source: Screen) {\n" +
            "                functionCallsStack.put(NAVIGATION_CALLED)\n" +
            "                screen = source\n" +
            "            }\n" +
            "\n" +
            "            companion object {\n" +
            "                private const val NAVIGATION_CALLED = \"navigation#map\"\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n"
    //endregion

    @Test
    fun test() {
        val codeGen = CodeGen()
        val expected = listOf(
            "package ru.easycode.words504.initial.domain\n" +
                    "\n" +
                    "interface InitialInteractor {\n" +
                    "\n" +
                    "    suspend fun init(): InitialResult\n" +
                    "}",
            "package ru.easycode.words504.initial.presentation\n" +
                    "\n" +
                    "interface InitialCommunication {\n" +
                    "\n" +
                    "    fun map(source: InitialUiState)\n" +
                    "}",
            "package ru.easycode.words504.initial.presentation\n" +
                    "\n" +
                    "interface NavigationCommunication {\n" +
                    "\n" +
                    "    fun map(source: Screen)\n" +
                    "}",
            "package ru.easycode.words504.initial.presentation\n" +
                    "\n" +
                    "import ru.easycode.words504.initial.domain.InitialInteractor\n" +
                    "import ru.easycode.words504.presentation.NavigationCommunication\n" +
                    "\n" +
                    "class InitialViewModel(\n" +
                    "    private val interactor: InitialInteractor,\n" +
                    "    private val communication: InitialCommunication,\n" +
                    "    private val navigation: NavigationCommunication\n" +
                    ")"
        )
        val actual: List<String> = codeGen.parse(source = source)
        assertEquals(expected, actual)
    }
}