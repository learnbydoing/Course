import sys
import math

data = [[2,0,4,3,0,1,0,2], [0,2,4,0,2,3,0,0], [4,0,1,3,0,1,0,1], [0,1,0,2,0,0,1,0], [0,0,2,0,0,4,0,0], [1,1,0,2,0,1,1,3], [2,1,3,4,0,2,0,2]]

def dot(v1, v2):
	s = 0	
	for i in range(len(v1)):
		s += (v1[i] * v2[i])
	return s
	
def get_centroid(matrix, cluster):
	M = len(matrix)
	k = len(cluster)
	centroid = [0.0 for x in range(M)]
	for i in range(M):
		for j in range(k):
			centroid[i] += matrix[i][cluster[j]]
		centroid[i] /= k
	return centroid

def get_item(matrix, index):
	M = len(matrix)
	N = len(matrix[0])
	item = [0 for x in range(M)]
	if index <= N:
		for i in range(M):
			item[i] = matrix[i][index]
	return item

def single_pass(matrix, threshold):
	M = len(matrix)
	N = len(matrix[0])

	cluster = [[]]	
	cluster[0].append(0)
		
	for i in range(1, N):
		k = len(cluster)
		index = 0
		max = 0
		item = get_item(matrix, i)
		for j in range(k):
			cen = get_centroid(matrix, cluster[j])			
			sim = dot(item, cen)
			if sim > max:
				max = sim
				index = j
		if max < threshold:
			cluster.append([i])
		else:
			cluster[index].append(i)

	for i in range(len(cluster)):
		print 'cluster{0}:\t'.format(i + 1) + ', '.join(str(x + 1) for x in cluster[i])

print 'Start single pass clustering based on dot product:'

single_pass(data, 10)