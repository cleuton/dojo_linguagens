from django.shortcuts import render

from rest_framework.response import Response
from rest_framework.decorators import api_view
from django.core.cache import cache
from .serializers import UrlSerializer

@api_view(['GET'])
def get_urls(request):
    urls = cache.get("urls", [])
    return Response({"urls": urls})

@api_view(['POST'])
def add_url(request):
    serializer = UrlSerializer(data=request.data)
    if serializer.is_valid():
        url = serializer.validated_data['url']
        urls = cache.get("urls", [])
        if url not in urls:
            urls.append(url)
            cache.set("urls", urls)
        return Response({"message": "URL added successfully"})
    return Response(serializer.errors, status=400)

