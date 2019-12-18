package smarthome.raspberry.home.api.domain.eventbus.events

open class Event

class NeedUser: Event()
class HasUser: Event()

class NeedHomeIdentifier: Event()
class HasHomeIdentifier: Event()


open class HomeLifecycleEvent: Event()
class Paused : HomeLifecycleEvent()
class Resumed : HomeLifecycleEvent()