import sys
import math

data = [[2,0,4,3,0,1,0,2], [0,2,4,0,2,3,0,0], [4,0,1,3,0,1,0,1], [0,1,0,2,0,0,1,0], [0,0,2,0,0,4,0,0], [1,1,0,2,0,1,1,3], [2,1,3,4,0,2,0,2]]

def cosine(v1, v2):
	s = 0.0
	s1 = 0.0
	s2 = 0.0
	for i in range(len(v1)):
		s += v1[i] * v2[i] * 1.0
		s1 += v1[i] **2 * 1.0
		s2 += v2[i] **2 * 1.0
	return s / (math.sqrt(s1 * s2))

def compare(c1, c2):
	len1 = len(c1)
	len2 = len(c2)
	if len1 != len2:
		return False
	
	for i in range(len1):
		if len(c1[i]) != len(c2[i]):
			return False			
		else:
			for j in range(len(c1[i])):
				if c1[i][j] != c2[i][j]:
					return False
	return True
	
def kmeans(matrix, cluster):
	M = len(matrix)
	N = len(matrix[0])
	
	K = len(cluster)

	for i in range(K):
		print 'cluster{0}:\t'.format(i + 1) + ', '.join(str(x + 1) for x in cluster[i])
	
	centroids = [[0.0 for x in range(N)] for x in range(K)]

	for i in range(K):
		cl = len(cluster[i])
		for j in range(N):
			for x in range(cl):
				centroids[i][j] += matrix[cluster[i][x]][j]
			centroids[i][j] /= cl

	#print 'cluster centroid:'
	#for i in range(K):	
	#	print 'C{0}:\t'.format(i + 1) + ', '.join(str(format(x,'.2f')) for x in centroids[i])
		
	similarity = [[0.0 for x in range(M)] for x in range(K)]

	for i in range(K):
		for j in range(M):		
			similarity [i][j] = cosine(matrix[j], centroids[i])

	newcluster = [[] for x in range(K)]
	for i in range(M):
		index = 0
		for j in range(1, K):
			if similarity[j][i] > similarity[index][i]:
				index = j
		newcluster[index].append(i)

	same = compare(newcluster, cluster)

	if same:
		print ''
		for i in range(K):
			print 'cluster{0}:\t'.format(i + 1) + ', '.join(str(x + 1) for x in cluster[i])
		print 'Not changed anymore, cluster result finalized'
	else:
		print ''
		kmeans(matrix, newcluster)
		
print 'Start kmeans clustering based on cosine similarity:'

cluster = [[0, 1], [4, 5, 6]]

kmeans(data, cluster)