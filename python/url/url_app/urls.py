from django.urls import path
from . import views

urlpatterns = [
    path('urls/', views.get_urls, name='get-urls'),
    path('urls/add/', views.add_url, name='add-url'),
]
